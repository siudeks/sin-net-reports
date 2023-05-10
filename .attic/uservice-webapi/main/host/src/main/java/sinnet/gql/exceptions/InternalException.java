package sinnet.gql.exceptions;

import io.smallrye.graphql.api.ErrorCode;
import lombok.experimental.StandardException;

@StandardException
@ErrorCode("1000")
public class InternalException extends RuntimeException {
    
}
