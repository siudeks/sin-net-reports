package net.onlex;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Data;
import lombok.experimental.Accessors;

@ApplicationScoped
@Data
@Accessors(fluent = true, chain = true)
public class UserLoginProps {

  @ConfigProperty(name = "APPLICATION_ID")
  private String applicationId;
  
  @ConfigProperty(name = "OPERATOR1_NAME")
  private String operator1Name;
  
  @ConfigProperty(name = "OPERATOR1_PASSWORD")
  private String operator1Password;

}
