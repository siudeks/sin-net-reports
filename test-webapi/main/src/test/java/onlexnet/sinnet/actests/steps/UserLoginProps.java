package onlexnet.sinnet.actests.steps;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.experimental.Accessors;

@Component
@Data
@Accessors(fluent = true, chain = true)
public class UserLoginProps {

  @Value("${APPLICATION_ID}")
  private String applicationId;
  
  @Value("${OPERATOR1_NAME}")
  private String operator1Name;
  
  @Value("${OPERATOR1_PASSWORD}")
  private String operator1Password;

}
