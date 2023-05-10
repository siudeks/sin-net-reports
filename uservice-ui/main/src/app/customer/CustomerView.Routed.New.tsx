import React from "react"
import { EntityId } from "../../store/actions/ServiceModel";
import { RootState } from "../../store/reducers";
import { Dispatch } from "redux";
import { connect, ConnectedProps } from "react-redux";
import { CustomerView, CustomerViewEntry } from "./CustomerView";
import { RouteComponentProps } from "react-router-dom";
import { useReserveCustomerMutation } from "../../Components/.generated/components";
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
  
interface CustomerViewNewProps extends PropsFromRedux, RouteComponentProps  {
}

  
export const CustomerViewNewLocal: React.FC<CustomerViewNewProps> = props => {
    const [reserveCustomerMutation, { data, called}] = useReserveCustomerMutation();

    if (!called) {
        reserveCustomerMutation({
            variables: {
                projectId: props.appState.projectId
            }
        });
    }

    if (data) {
        const id: EntityId = data.Customers.reserve;
        const entry: CustomerViewEntry = {
            KlientNazwa: 'Nowy klient',
            autoryzacje: [],
            autoryzacjeEx: [],
            kontakty: []
        }
        const itemSaved = () => props.history.push(routing.customers);
        const itemRemoved = () => props.history.push(routing.customers);
        return <CustomerView id={id} entry={entry} itemSaved={itemSaved} itemRemoved={itemRemoved} />;
    }

    return null;
}

export const CustomerViewNew = connect(mapStateToProps, mapDispatchToProps)(CustomerViewNewLocal);