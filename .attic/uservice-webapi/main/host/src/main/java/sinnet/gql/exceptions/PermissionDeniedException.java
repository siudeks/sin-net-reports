package sinnet.gql.exceptions;

import io.smallrye.graphql.api.ErrorCode;
import lombok.experimental.StandardException;

@StandardException
@ErrorCode("1001")
public class PermissionDeniedException extends RuntimeException {
    
}
