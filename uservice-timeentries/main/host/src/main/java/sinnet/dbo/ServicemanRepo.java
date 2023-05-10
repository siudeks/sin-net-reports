package sinnet.dbo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.vavr.collection.List;

/**
 * TBD.
 */
@Repository
interface ServicemanRepo extends JpaRepository<ServicemanDbo, UUID> {

  List<ServicemanDbo> findByProjectId(UUID projectId);
  
}
