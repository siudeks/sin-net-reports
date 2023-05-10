import React from "react"
import { EntityId } from "../../store/actions/ServiceModel";
import { RootState } from "../../store/reducers";
import { Dispatch } from "redux";
import { connect, ConnectedProps } from "react-redux";
import { RouteComponentProps } from "react-router-dom";
import { CustomerViewEdit } from "./CustomerView.Edit";
import { routing } from "../../Routing";


const mapStateToProps = (state: RootState) => {
    if (state.appState.empty) {
        throw new Error('Invalid state');
    }
    return state;
}

const mapDispatchToProps = (dispatch: Dispatch) => {
    return { }
}

const connector = connect(mapStateToProps, mapDispatchToProps);
type PropsFromRedux = ConnectedProps<typeof connector>;
  
interface CustomerViewEditProps extends PropsFromRedux, RouteComponentProps<{ projectId: string, entityId: string, entityVersion: string}> {
}

  
export const CustomerViewRoutedEditLocal: React.FC<CustomerViewEditProps> = props => {
    const id: EntityId = {
        projectId: props.match.params.projectId,
        entityId: props.match.params.entityId,
        entityVersion: Number(props.match.params.entityVersion)
    };

    const itemSaved = () => props.history.push(routing.customers);
    const itemRemoved = () => props.history.push(routing.customers)
    return <CustomerViewEdit id={id} itemSaved={itemSaved}
                                     itemRemoved={itemRemoved} />;
}

export const CustomerViewRoutedEdit = connect(mapStateToProps, mapDispatchToProps)(CustomerViewRoutedEditLocal);