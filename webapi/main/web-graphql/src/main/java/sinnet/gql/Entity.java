package sinnet.gql;


import java.util.UUID;

public interface Entity {
    UUID getProjectId();
    UUID getEntityId();
    int getEntityVersion();
}
