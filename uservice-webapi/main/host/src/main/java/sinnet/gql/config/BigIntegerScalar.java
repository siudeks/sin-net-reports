package sinnet.gql.config;

import java.math.BigInteger;
import java.time.format.DateTimeParseException;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import jakarta.validation.constraints.NotNull;

final class BigIntegerScalar implements Coercing<BigInteger, String> {

  @Override
  public String serialize(@NotNull Object dataFetcherResult) throws CoercingSerializeException {
    if (dataFetcherResult instanceof BigInteger) {
      return ((BigInteger) dataFetcherResult).toString();
    } else {
      throw new CoercingSerializeException(
          "Expected a 'BigInteger' but was '" + dataFetcherResult.getClass().getName() + "'.");
    }
  }

  @Override
  public @NotNull BigInteger parseValue(@NotNull Object input) throws CoercingParseValueException {
    try {
      if (input instanceof String) {
        return new BigInteger((String) input);
      } else {
        throw new CoercingParseValueException("Expected a valid 'BigInteger' value but was '" + input + "'.");
      }
    } catch (DateTimeParseException e) {
      throw new CoercingParseValueException("Expected a valid 'BigInteger' value but was '" + input + "'.");
    }
  }

  @Override
  public @NotNull BigInteger parseLiteral(@NotNull Object input) throws CoercingParseLiteralException {
    if (input instanceof StringValue) {
      try {
        var value = ((StringValue) input).getValue();
        return new BigInteger(value);
      } catch (DateTimeParseException e) {
        throw new CoercingParseLiteralException("Expected a valid 'BigInteger' value but was '" + input + "'.");
      }
    } else {
      throw new CoercingParseLiteralException("Expected 'StringValue' but was '" + input.getClass().getName() + "'.");
    }
  }

  static GraphQLScalarType newScalar() {
    return GraphQLScalarType.newScalar()
        .name("BigInteger")
        .coercing(new BigIntegerScalar())
        .build();
  }
}
