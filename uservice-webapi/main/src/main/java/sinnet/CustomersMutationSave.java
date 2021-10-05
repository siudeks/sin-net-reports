package sinnet;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Source;

@GraphQLApi
@ApplicationScoped
public class CustomersMutationSave {

  public SomeEntity save(@Source CustomersMutation self,
                         @NonNull MyEntity id,
                         @NonNull CustomerInput entry,
                         @NonNull CustomerSecretInput[] secrets,
                         @NonNull CustomerSecretExInput[] secretsEx,
                         @NonNull CustomerContactInput[] contacts) {
    return null;
  }
    
}