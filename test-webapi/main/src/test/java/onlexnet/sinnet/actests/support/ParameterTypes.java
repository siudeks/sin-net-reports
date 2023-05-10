package onlexnet.sinnet.actests.support;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import io.cucumber.java.ParameterType;

@Component
public class ParameterTypes {

  @ParameterType("user[1-9]|operator[1-9]")
  public UserEmail userName(String name) {
    var randomPart = RandomStringUtils.randomAlphabetic(6);
    var randomEmail = name + "@" + randomPart + ".example";
    return new UserEmail(name, randomEmail);
  }

  @ParameterType("project[1-9]")
  public Project projectName(String name) {
    return new Project(name);
  }
}
