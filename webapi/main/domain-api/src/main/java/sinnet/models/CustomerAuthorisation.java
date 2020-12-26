package sinnet.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@AllArgsConstructor
@Value
@JsonDeserialize(builder = CustomerAuthorisation.MyBuilder.class)
@Builder(builderClassName = "MyBuilder", toBuilder = true)
public final class CustomerAuthorisation {
    private String location;
    private String username;
    private String password;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MyBuilder {
    }
}
