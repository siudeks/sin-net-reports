import React from "react"
import { EntityId } from "../../store/actions/ServiceModel";
import { RootState } from "../../store/reducers";
import { Dispatch } from "redux";
import { connect, ConnectedProps } from "react-redux";
import { ContactDetails, CustomerView, CustomerViewEntry, SecretExModel, SecretModel } from "./CustomerView";
import { useGetCustomerQuery } from "../../Components/.generated/components";
import _ from "lodash";
import { v1 as uuid } from 'uuid';
import { SecretsTimestamp } from "./SecretsTimestamp";

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
  
interface CustomerViewEditProps extends PropsFromRedux {
    id: EntityId,
    itemSaved: () => void
    itemRemoved: () => void
}

  
export const CustomerViewEditLocal: React.FC<CustomerViewEditProps> = props => {
    const { data, error } = useGetCustomerQuery({
        variables: {
            projectId: props.appState.projectId,
            entityId: props.id.entityId
        }
    })
    
    if(error) {
        return <div>Error: {JSON.stringify(error)}</div>;
    }

    if (data) {
        const id = data.Customers.get?.id
        const input = data.Customers.get;
        if (!id) {
            return <div>No data</div>;
        }
        if (!input) {
            return <div>No valid data</div>;
        }

        const autoryzacje = _.chain(input.secrets)
            .map(it => {
                const ret: SecretModel = {
                    localKey: uuid(),
                    location: it.location,
                    username: it.username ?? undefined,
                    password: it.password ?? undefined,
                    who: it.changedWho,
                    when: SecretsTimestamp.of(it.changedWhen)
                };
                return ret;
            })
            .value();

        const autoryzacjeEx = _.chain(input.secretsEx)
        .map(it => {
            const ret: SecretExModel = {
                localKey: uuid(),
                location: it.location,
                username: it.username ?? undefined,
                password: it.password ?? undefined,
                entityName: it.entityName ?? undefined,
                entityCode: it.entityCode ?? undefined,
                who: it.changedWho,
                when: SecretsTimestamp.of(it.changedWhen)
            };
            return ret;
        })
        .value();

        const kontakty = _.chain(input.contacts)
        .map(it => {
            const ret: ContactDetails = {
                localKey: uuid(),
                firstName: it.firstName ?? undefined,
                lastName: it.lastName ?? undefined,
                phoneNo: it.phoneNo ?? undefined,
                email: it.email ?? undefined
            }
            return ret;
        })
        .value();

        const entry: CustomerViewEntry = {
            EmailOfOperator: input?.data.operatorEmail ?? undefined,
            ModelOfBilling: input?.data.billingModel ?? undefined,
            ModelOfSupport: input?.data.supportStatus ?? undefined,
            Distance: input?.data.distance ?? undefined,
            KlientNazwa: input?.data.customerName ?? 'Nowy klient',
            KlientMiejscowosc: input?.data.customerCityName ?? undefined,
            KlientAdres: input?.data.customerAddress ?? undefined,
            nfzUmowa: input?.data.nfzUmowa ?? undefined,
            nfzMaFilie: input?.data.nfzMaFilie ?? undefined,
            nfzLekarz: input?.data.nfzLekarz ?? undefined,
            nfzPolozna: input?.data.nfzPolozna ?? undefined,
            nfzPielegniarkaSrodowiskowa: input?.data.nfzPielegniarkaSrodowiskowa ?? undefined,
            nfzMedycynaSzkolna: input?.data.nfzMedycynaSzkolna ?? undefined,
            nfzTransportSanitarny: input?.data.nfzTransportSanitarny ?? undefined,
            nfzNocnaPomocLekarska: input?.data.nfzNocnaPomocLekarska ?? undefined,
            nfzAmbulatoryjnaOpiekaSpecjalistyczna: input?.data.nfzAmbulatoryjnaOpiekaSpecjalistyczna ?? undefined,
            nfzRehabilitacja: input?.data.nfzRehabilitacja ?? undefined,
            nfzStomatologia: input?.data.nfzStomatologia ?? undefined,
            nfzPsychiatria: input?.data.nfzPsychiatria ?? undefined,
            nfzSzpitalnictwo: input?.data.nfzSzpitalnictwo ?? undefined,
            nfzProgramyProfilaktyczne: input?.data.nfzProgramyProfilaktyczne ?? undefined,
            nfzZaopatrzenieOrtopedyczne: input?.data.nfzZaopatrzenieOrtopedyczne ?? undefined,
            nfzOpiekaDlugoterminowa: input?.data.nfzOpiekaDlugoterminowa ?? undefined,
            nfzNotatki: input?.data.nfzNotatki ?? undefined,
            komercjaJest: input?.data.komercjaJest ?? undefined,
            komercjaNotatki: input?.data.komercjaNotatki ?? undefined,
            daneTechniczne: input?.data.daneTechniczne ?? undefined,
            autoryzacje,
            autoryzacjeEx,
            kontakty
        }
        return <CustomerView id={id} entry={entry} 
                             itemSaved={props.itemSaved}
                             itemRemoved={props.itemRemoved}/>;
    }

    return <div>Loading customer details...</div>
}

export const CustomerViewEdit = connect(mapStateToProps, mapDispatchToProps)(CustomerViewEditLocal);