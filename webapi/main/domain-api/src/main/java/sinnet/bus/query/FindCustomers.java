package sinnet.bus.query;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sinnet.bus.JsonMessage;
import sinnet.models.CustomerValue;

public interface FindCustomers {

    @Data
    class Ask implements JsonMessage {
        /** Address used to send the query to it's handler. */
        public static final String ADDRESS = "query.FindCustomers";

        private boolean ignored;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Reply implements JsonMessage {
        private CustomerData[] data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class CustomerData {
        private UUID entityId;
        private int entityVersion;
        private CustomerValue value;
    }
}