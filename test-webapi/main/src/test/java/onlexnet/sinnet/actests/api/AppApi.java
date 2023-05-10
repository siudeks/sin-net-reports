package onlexnet.sinnet.actests.api;

@GraphQLClientApi(configKey = "local-sinnetapp") // - in case of testing local stack
public interface AppApi extends AppApiQuery, AppApiMutation {

}
