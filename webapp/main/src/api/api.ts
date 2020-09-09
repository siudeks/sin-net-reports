import { createHttpLink } from "apollo-link-http";
import { addressProvider } from "../addressProvider";
import { ApolloLink } from "apollo-link";
import { ApolloClient } from "apollo-client";
import { InMemoryCache } from "apollo-cache-inmemory";
import { getSdk } from "./generated";
import { GraphQLClient } from 'graphql-request';

// const wsLink = new WebSocketLink({
//   uri: "wss://raport.sin.net.pl/subscriptions",
//   options: {
//     reconnect: false,
//   },
// });

const graphqlUrl = `${addressProvider().host}/graphql`;

export const apolloClientFactory = (jwtIdToken: string) => {
    // configuration below is focused on Authentication
    // https://www.apollographql.com/docs/react/networking/authentication/
  
    const httpLink = createHttpLink({
      uri: graphqlUrl,
  
      // Cookie
      // If your app is browser based and you are using cookies for login and session management with a backend,
      // it's very easy to tell your network interface to send the cookie along with every request.
      // You just need to pass the credentials option. e.g. credentials: 'same-origin' as shown below,
      // if your backend server is the same domain or else credentials: 'include' if your backend is a different domain.
      credentials: "include",
    });
  
    const middlewareAuthLink = new ApolloLink((operation, forward) => {
      operation.setContext({
        headers: {
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Credentials": true,
          Authorization: `Bearer ${jwtIdToken}`,
        },
      });
      return forward(operation);
    });
  
    var address = addressProvider().host;
    return new ApolloClient({
      cache: new InMemoryCache({}),
      link: middlewareAuthLink.concat(httpLink),
      connectToDevTools: true
    });
  };

  export const graphQlClient = new GraphQLClient(graphqlUrl);
  export const apolloClient = apolloClientFactory("");
  export const sdk = getSdk(graphQlClient);