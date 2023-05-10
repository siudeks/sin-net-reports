import React from "react";

import { ApolloProvider } from "@apollo/react-hooks";
import { apolloClientFactory } from "../api";
import { RootState } from "../store/reducers";
import { Dispatch } from "redux";
import { connect, ConnectedProps } from "react-redux";
import { MainView } from "./mainview/MainView";

const mapStateToProps = (state: RootState) => {
  return state.auth;
}
const mapDispatchToProps = (dispatch: Dispatch) => {
  return {
  }
}
const connector = connect(mapStateToProps, mapDispatchToProps);
type PropsFromRedux = ConnectedProps<typeof connector>;



interface Props extends PropsFromRedux {
}

const ViewLocal: React.FC<Props> = (props) => {

  const client = apolloClientFactory(props.idToken);

  return (
    <ApolloProvider client={client}>
      <MainView />
    </ApolloProvider>
  );
};

export const View = connect(mapStateToProps, mapDispatchToProps)(ViewLocal);