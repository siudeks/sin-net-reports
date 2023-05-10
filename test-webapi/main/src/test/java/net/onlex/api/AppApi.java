package net.onlex.api;

import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;

// headers: https://smallrye.io/smallrye-graphql/1.4.2/typesafe-client-headers/

// @GraphQLClientApi(configKey = "beta-sinnetapp") // - in case of testing remove env
@GraphQLClientApi(configKey = "local-sinnetapp") // - in case of testing local stack
public interface AppApi extends AppApiQuery, AppApiMutation {

}
