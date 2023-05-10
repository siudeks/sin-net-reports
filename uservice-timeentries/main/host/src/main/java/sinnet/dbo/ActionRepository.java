package sinnet.dbo;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TBD.
 */
@Repository
interface ActionRepository extends JpaRepository<ActionDbo, UUID> {
  
  void deleteByProjectIdAndEntityIdAndEntityVersion(UUID projectId, UUID entityId, long version);

  ActionDbo findByProjectIdAndEntityIdAndEntityVersion(UUID projectId, UUID entityId, long version);

  ActionDbo findByProjectIdAndEntityId(UUID projectId, UUID entityId);

  List<ActionDbo> findByProjectIdAndDateGreaterThanEqualAndDateLessThanEqual(UUID projectId, LocalDate from, LocalDate to);
}
