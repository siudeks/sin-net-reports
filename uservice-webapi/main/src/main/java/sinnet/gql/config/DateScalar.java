package sinnet.gql.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import jakarta.validation.constraints.NotNull;

final class DateScalar implements Coercing<LocalDate, String> {

  @Override
  public String serialize(@NotNull Object dataFetcherResult) throws CoercingSerializeException {
    if (dataFetcherResult instanceof LocalDate) {
      return ((LocalDate) dataFetcherResult).toString();
    } else {
      throw new CoercingSerializeException(
          "Expected a 'LocalDate' but was '" + dataFetcherResult.getClass().getName() + "'.");
    }
  }

  @Override
  public @NotNull LocalDate parseValue(@NotNull Object input) throws CoercingParseValueException {
    try {
      if (input instanceof String) {
        return LocalDate.parse((String) input);
      } else {
        throw new CoercingParseValueException("Expected a valid 'LocalDate' value but was '" + input + "'.");
      }
    } catch (DateTimeParseException e) {
      throw new CoercingParseValueException("Expected a valid 'LocalDate' value but was '" + input + "'.");
    }
  }

  @Override
  public @NotNull LocalDate parseLiteral(@NotNull Object input) throws CoercingParseLiteralException {
    if (input instanceof StringValue) {
      try {
        var value = ((StringValue) input).getValue();
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
      } catch (DateTimeParseException e) {
        throw new CoercingParseLiteralException("Expected a valid 'LocalDate' value but was '" + input + "'.");
      }
    } else {
      throw new CoercingParseLiteralException("Expected 'StringValue' but was '" + input.getClass().getName() + "'.");
    }
  }

  static GraphQLScalarType newScalar() {
    return GraphQLScalarType.newScalar()
        .name("Date")
        .coercing(new DateScalar())
        .build();
  }
}
