package sinnet.gql.config;

import javax.annotation.ParametersAreNonnullByDefault;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;

/**
 * This class replace the default GQL error handling, as the default one does
 * not log exceptions,
 * just send them back.
 */
@Component
@Slf4j
@ParametersAreNonnullByDefault
class GqlExceptionHandler extends DataFetcherExceptionResolverAdapter {

  @Override
  protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {

    var errorMessage = ex.getMessage();

    log.error(errorMessage, ex);

    return GraphqlErrorBuilder.newError()
        .errorType(ErrorType.INTERNAL_ERROR)
        .message(errorMessage)
        .path(env.getExecutionStepInfo().getPath())
        .location(env.getField().getSourceLocation())
        .build();
  }
}
