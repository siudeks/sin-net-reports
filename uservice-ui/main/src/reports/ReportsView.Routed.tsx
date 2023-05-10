import React from "react"
import { connect, ConnectedProps } from "react-redux";
import { RouteComponentProps } from "react-router-dom";
import { Dispatch } from "redux";
import { RootState } from "../store/reducers";
import { ReportsView } from "./ReportsView";

const mapStateToProps = (state: RootState) => {
    return { appState: state.appState, viewContext: state.viewContext };
}
const mapDispatchToProps = (dispatch: Dispatch) => {
    return { };
}

const connector = connect(mapStateToProps, mapDispatchToProps);
type PropsFromRedux = ConnectedProps<typeof connector>;


interface ReportsViewRoutedProps extends PropsFromRedux {
}


const ReportsViewRoutedLocal: React.FC<ReportsViewRoutedProps> = props => {
    const projectId = props.appState.projectId;
    const { dateFrom } = props.viewContext.period.getValue();
    return <ReportsView projectId={projectId} from={dateFrom} />

}

export const ReportsViewRouted = connect(mapStateToProps, mapDispatchToProps)(ReportsViewRoutedLocal);