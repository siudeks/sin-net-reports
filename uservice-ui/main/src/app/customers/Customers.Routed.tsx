import React from "react";
import { connect, ConnectedProps } from "react-redux";
import { RouteComponentProps } from "react-router-dom";
import { Dispatch } from "redux";
import { useListCustomers } from "../../api/useListCustomers";
import { routing } from "../../Routing";
import { RootState } from "../../store/reducers";
import { CustomersView } from "./Customers";


const mapStateToProps = (state: RootState) => {
    if (state.appState.empty) {
        throw new Error('Invalid state');
    }
    return state;
}

const mapDispatchToProps = (dispatch: Dispatch) => {
    return {}
}

const connector = connect(mapStateToProps, mapDispatchToProps);
type PropsFromRedux = ConnectedProps<typeof connector>;

interface CustomersProps extends PropsFromRedux, RouteComponentProps {
}

const CustomersRoutedView: React.FC<CustomersProps> = (props) => {
    const projectId = props.appState.projectId;
    const newCustomerCommand = () => props.history.push(routing.newCustomer);
    return (<CustomersView givenProjectId={projectId}
        onNewClientCommand={newCustomerCommand}
        listCustomers={useListCustomers} />);
}



export const CustomersRoutedConnectedView = connect(mapStateToProps, mapDispatchToProps)(CustomersRoutedView);