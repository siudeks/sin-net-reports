import { Label, Stack } from "@fluentui/react";
import React from "react";
import { connect, ConnectedProps } from "react-redux";
import { RouteComponentProps } from "react-router-dom";
import { Dispatch } from "redux";
import { routing } from "../Routing";
import { RootState } from "../store/reducers";
import { Content } from "./ActionList";
import { ServiceCommandBar } from "./Commands";

const mapStateToProps = (state: RootState) => {
  return ({ viewContext: state.viewContext, appState: state.appState });
}
const mapDispatchToProps = (dispatch: Dispatch) => ({});

const connector = connect(mapStateToProps, mapDispatchToProps);
type PropsFromRedux = ConnectedProps<typeof connector>;

interface MainProps extends PropsFromRedux, RouteComponentProps {
}

const MainView: React.FC<MainProps> = (props) => {

  return (
    <Stack styles={{
      root: { width: "100%", height: "100%" }
    }}>
      <Stack.Item>
        <ServiceCommandBar
          onReportsViewRequested={() => {
            const url = routing.reports;
            props.history.push(url);
          }}
           />
      </Stack.Item>
      <Stack.Item  >
        <div style={{ padding: "10px" }}>
          <Label >
            {"Miesiąc: " + props.viewContext.period.toString()}
          </Label>
        </div>
      </Stack.Item>
      <Stack.Item verticalFill
        styles={{
          root: {
            height: "100%",
            overflowY: "auto",
            overflowX: "auto",
          },
        }}>
        <Content />
      </Stack.Item>

    </Stack>
  );
};

export const ServicesDefault = connect(mapStateToProps, mapDispatchToProps)(MainView);
