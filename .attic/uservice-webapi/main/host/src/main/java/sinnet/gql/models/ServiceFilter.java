package sinnet.gql.models;

import java.time.LocalDate;

import org.eclipse.microprofile.graphql.NonNull;

import lombok.Data;

@Data
public class ServiceFilter {
  @NonNull
  private LocalDate from;
  @NonNull
  private LocalDate to;
}
