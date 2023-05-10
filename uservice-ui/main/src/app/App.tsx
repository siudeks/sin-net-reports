import './App.css';
import React from "react";

import { View as AuthenticatedView } from "./AppAuthenticated";
import { View as UnauthenticatedView } from "./AppUnauthenticated";
import InProgressView from "./AppInProgress";
import { Dispatch } from "redux";
import { connect, ConnectedProps } from "react-redux";
import { SignInFlow } from "../store/session/types";
import { initiateSession } from "../store/session/actions";
import { RootState } from "../store/reducers";
import { Configuration, LogLevel, PublicClientApplication } from "@azure/msal-browser";
import { MsalProvider } from "@azure/msal-react";

const mapStateToProps = (state: RootState) => {
  return state.auth;
}
const mapDispatchToProps = (dispatch: Dispatch) => {
  return {
    login: () => {
      dispatch(initiateSession());
    }
  }
}
const connector = connect(mapStateToProps, mapDispatchToProps);
type PropsFromRedux = ConnectedProps<typeof connector>;

interface AppProps extends PropsFromRedux {
}

const config: Configuration = {
  auth: {
      clientId: "36305176-2249-4ce5-8d59-a91dd7363610", // sinnetapp-prod
      authority: "https://sinnetapp.b2clogin.com/7c86200b-9308-4ebc-a462-fab0a67b91e6/B2C_1_sign-in-or-up",
      // navigateToLoginRequestUrl: true,
      // postLogoutRedirectUri: 'https://raport.sin.net.pl/',
      knownAuthorities: [
          "sinnetapp.b2clogin.com"
      ]
  },
  cache: {
      cacheLocation: "sessionStorage", // This configures where your cache will be stored
      storeAuthStateInCookie: false, // Set this to "true" if you are having issues on IE11 or Edge
  },
  system: {
      loggerOptions: {
          loggerCallback: (level, message, containsPii) => {
              if (containsPii) {
                  return;
              }
              switch (level) {
                  case LogLevel.Error:
                      console.error(message);
                      return;
                  case LogLevel.Info:
                      console.info(message);
                      return;
                  case LogLevel.Verbose:
                      console.debug(message);
                      return;
                  case LogLevel.Warning:
                      console.warn(message);
                      return;
              }
          }
      }
  }
};

const pca = new PublicClientApplication(config);

const App: React.FC<AppProps> = props => {

  switch (props.flow) {
    case SignInFlow.Unknown:
    case SignInFlow.SessionInitiated:
      return (<MsalProvider instance={pca}><InProgressView /></MsalProvider>);
    case SignInFlow.SessionEstablished:
      return (<MsalProvider instance={pca}><AuthenticatedView /></MsalProvider>);
    default:
      return <UnauthenticatedView login={props.login} />;
  }
};

export default connector(App)

