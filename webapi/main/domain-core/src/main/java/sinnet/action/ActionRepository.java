package sinnet.action;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends CrudRepository<ActionDbModel, UUID> {

    @Query("SELECT it FROM ActionDbModel it WHERE it.when >= :from AND it.when <= :to")
    List<ActionDbModel> findForPeriod(LocalDate from, LocalDate to);
}
