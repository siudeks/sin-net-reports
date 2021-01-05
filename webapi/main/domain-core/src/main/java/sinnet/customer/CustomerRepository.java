package sinnet.customer;

import java.util.UUID;

import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vertx.core.Future;
import lombok.Value;
import sinnet.models.CustomerSecret;
import sinnet.models.CustomerSecretEx;
import sinnet.models.CustomerContact;
import sinnet.models.CustomerValue;
import sinnet.models.EntityId;

interface CustomerRepository {

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

    /**
     * Returns a model pointed by given {@code id} or empty value if the model does not exists.
     * @param id if of the requested model.
     */
    Future<Option<CustomerModel>> get(EntityId id);

    /**
     * Returns a latest model pointed by given {@code projectId} {@code id} or empty value if the model does not exists.
     * @param id if of the requested model.
     */
    Future<Option<CustomerModel>> get(UUID projectId, UUID id);

    Future<List<CustomerModel>> list(UUID projectId);

    Future<Boolean> remove(EntityId id);

    @Value
    class CustomerModel {
        private EntityId id;
        private CustomerValue value;
        private CustomerSecret[] secrets;
        private CustomerSecretEx[] secretsEx;
        private CustomerContact[] contacts;
    }
}
