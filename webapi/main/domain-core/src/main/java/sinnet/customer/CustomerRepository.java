package sinnet.customer;

import io.vertx.core.Future;
import sinnet.models.CustomerContact;
import sinnet.models.CustomerSecret;
import sinnet.models.CustomerSecretEx;
import sinnet.models.CustomerValue;
import sinnet.models.EntityId;
import sinnet.read.CustomerProjection;

public interface CustomerRepository extends CustomerProjection {

    /**
     * Saves a new version of Entity identified by given eid.
     * <p>
     * @return new EID for just stored entity.
     */
    Future<EntityId> write(EntityId eid,
                           CustomerValue value,
                           CustomerSecret[] secrets,
                           CustomerSecretEx[] secretsEx,
                           CustomerContact[] contacts);

    Future<Boolean> remove(EntityId id);
}
