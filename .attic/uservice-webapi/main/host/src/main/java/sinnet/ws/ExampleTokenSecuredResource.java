package sinnet.ws;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.security.Authenticated;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;

/** TODO: delete. */
@Path("/secured")
public class ExampleTokenSecuredResource {

  @Inject
  JsonWebToken jwt;

  @GET()
  @Path("permit-all")
  @PermitAll
  @Produces(MediaType.TEXT_PLAIN)
  public String hello(@Context SecurityContext ctx) {
    return getResponseString(ctx);
  }

  @GET
  @Path("debug")
  @PermitAll
  @Produces(MediaType.TEXT_PLAIN)
  public String debug(@Context SecurityContext ctx) {
    return Integer.toString(jwt.hashCode());
  }

  @GET
  @Path("roles-allowed") 
  @RolesAllowed({ "User", "Admin" }) 
  @Produces(MediaType.TEXT_PLAIN)
  public String helloRolesAllowed(@Context SecurityContext ctx) {
    return getResponseString(ctx) + ", birthdate: " + jwt.getClaim("birthdate").toString(); 
  }

  @GET
  @Path("authenticated")
  @Authenticated
  @Produces(MediaType.TEXT_PLAIN)
  public String authentiocated(@Context SecurityContext ctx) {
    var principal = (DefaultJWTCallerPrincipal) ctx.getUserPrincipal();
    return principal.getClaimNames().toString();
  }

  private String getResponseString(SecurityContext ctx) {
    String name;
    if (ctx.getUserPrincipal() == null) {
      name = "anonymous";
    } else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) {
      throw new IllegalStateException("Principal and JsonWebToken names do not match");
    } else {
      name = ctx.getUserPrincipal().getName();
    }
    return String.format("hello + %s,"
            + " isHttps: %s,"
            + " authScheme: %s,"
            + " hasJWT: %s",
            name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt());
  }

  private boolean hasJwt() {
    return jwt.getClaimNames() != null;
  }
}
