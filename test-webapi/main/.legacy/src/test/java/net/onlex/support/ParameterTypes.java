package net.onlex.support;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang3.RandomStringUtils;

import io.cucumber.java.ParameterType;

@ApplicationScoped
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
