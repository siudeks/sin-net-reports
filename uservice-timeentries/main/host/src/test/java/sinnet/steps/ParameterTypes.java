package sinnet.steps;

import io.cucumber.java.ParameterType;
import sinnet.models.ValName;

public class ParameterTypes {

  @ParameterType(".+")
  public ValName projectAlias(String value) {
    return ValName.of(value);
  }

  @ParameterType(".+")
  public ValName operatorAlias(String value) {
    return ValName.of(value);
  }
}
