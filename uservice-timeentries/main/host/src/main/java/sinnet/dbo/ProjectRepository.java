package sinnet.dbo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.vavr.collection.Seq;

@Repository
interface ProjectRepository extends JpaRepository<ProjectDbo, UUID> {

  Seq<ProjectDbo> findByEmailOfOwner(String emailOfOwner);

  long countByEmailOfOwner(String emailOfOwner);
}
