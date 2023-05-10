package onlexnet.sinnet.actests.steps;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.microsoft.aad.msal4j.PublicClientApplication;
import com.microsoft.aad.msal4j.UserNamePasswordParameters;

import lombok.SneakyThrows;

@Component
public class AzureAD {

  /**
   * 
   * @param props
   * @return idToken
   */
  @SneakyThrows
  public String signIn(UserLoginProps props) {
    // https://docs.microsoft.com/en-us/azure/active-directory-b2c/b2clogin
    // https://docs.microsoft.com/en-us/azure/active-directory/develop/msal-authentication-flows#usernamepassword-ropc
    // https://github.com/AzureAD/microsoft-authentication-library-for-dotnet/wiki/AAD-B2C-specifics#resource-owner-password-credentials-ropc-with-b2c
    // about openid and profile: https://auth0.com/docs/get-started/apis/scopes/sample-use-cases-scopes-and-claims

    var azureADB2CHostname = "sinnetapp.b2clogin.com";
    var tenantId = "7c86200b-9308-4ebc-a462-fab0a67b91e6";
    var policyName = "b2c_1_ropc";
    var authorityHost = String.format("https://%s/tfp/%s/%s", azureADB2CHostname, tenantId, policyName);

    var pca = PublicClientApplication.builder(props.applicationId())
        .b2cAuthority(authorityHost)
        .build();

    var params = UserNamePasswordParameters
        // adding below "openid", "profile" implicitly is not necessary
        // as they are added automatically by msal
        // but I wanted to make list of scopess less confusing as list with just applicationId (mandatory and I still
        // don't know why) would be unclear
        .builder(Stream.of("openid", "profile", props.applicationId()).collect(Collectors.toSet()),
          props.operator1Name(),
          props.operator1Password().toCharArray())
        .tenant(tenantId)
        .build();
    var tokenPromise = pca.acquireToken(params);
    return tokenPromise.join().idToken();
  }
}
