import { ApolloClient, ApolloLink, createHttpLink, DefaultOptions, InMemoryCache } from "@apollo/client";
import { GraphQLClient } from 'graphql-request';
import { addressProvider } from "../addressProvider";

const graphqlUrl = `${addressProvider().host}/graphql`;

export const apolloClientFactory = (jwtToken: string) => {
  // configuration below is focused on Authentication
  // https://www.apollographql.com/docs/react/networking/authentication/

  const httpLink = createHttpLink({
    uri: graphqlUrl,

    // Cookie
    // If your app is browser based and you are using cookies for login and session management with a backend,
    // it's very easy to tell your network interface to send the cookie along with every request.
    // You just need to pass the credentials option. e.g. credentials: 'same-origin' as shown below,
    // if your backend server is the same domain or else credentials: 'include' if your backend is a different domain.
    // credentials: "",
  });

  const middlewareAuthLink = new ApolloLink((operation, forward) => {
    operation.setContext({
      headers: {
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Credentials": true,
        "Access-Control-Allow-Methods": "OPTIONS, GET, POST, PUT, PATCH, DELETE",
        Authorization: `Bearer ${jwtToken}`,
      },
    });
    return forward(operation);
  });

  const defaultOptions: DefaultOptions = {
    watchQuery: {
      fetchPolicy: 'no-cache',
      errorPolicy: 'ignore',
    },
    query: {
      fetchPolicy: 'network-only',
      errorPolicy: 'all',
    },
  }
  return new ApolloClient({
    cache: new InMemoryCache({}),
    link: middlewareAuthLink.concat(httpLink),
    connectToDevTools: true,
    defaultOptions
  });
};

export const graphQlClient = new GraphQLClient(graphqlUrl);
export const apolloClient = apolloClientFactory("");
