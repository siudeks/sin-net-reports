import { Checkbox, ComboBox, DefaultButton, IComboBox, IComboBoxOption, IStackStyles, IStackTokens, ITextFieldStyles, PrimaryButton, ScrollablePane, ScrollbarVisibility, Separator, Spinner, SpinnerSize, Stack, TextField } from "@fluentui/react";
import _ from "lodash";
import React, { MouseEventHandler } from "react";
import { v1 as uuid } from 'uuid';
import { useGetUsers } from "../../api/useGetUsers";
import { CustomerContactInput, CustomerInput, CustomerSecretExInput, CustomerSecretInput, useRemoveCustomerMutation, useSaveCustomerMutation } from "../../Components/.generated/components";
import { EntityId } from "../../store/actions/ServiceModel";
import { NewContactItem } from "./NewContactItem";
import { SecretsTimestamp } from "./SecretsTimestamp";
import { NewSecret } from "./View.NewSecret";
import { UserPasswordItem } from "./View.UserPasswordItem";
import { UserPasswordItemExt } from "./View.UserPasswordItemEx";


const stackStyles: Partial<IStackStyles> = { root: { width: 650 } };
const stackTokens: IStackTokens = { childrenGap: 12 };

const HorizontalSeparatorStack = (props: { children: JSX.Element[] }) => (
    <>
        {React.Children.map(props.children, child => {
            return <Stack tokens={stackTokens}>{child}</Stack>;
        })}
    </>
);

const narrowTextFieldStyles: Partial<ITextFieldStyles> = { fieldGroup: { width: 100 } };

export interface CustomerViewEntry {
    EmailOfOperator?: string,
    ModelOfSupport?: string
    ModelOfBilling?: string,
    Distance?: number,
    KlientNazwa: string,
    KlientMiejscowosc?: string,
    KlientAdres?: string
    nfzUmowa?: boolean,
    nfzMaFilie?: boolean,
    nfzLekarz?: boolean,
    nfzPolozna?: boolean,
    nfzPielegniarkaSrodowiskowa?: boolean,
    nfzMedycynaSzkolna?: boolean,
    nfzTransportSanitarny?: boolean,
    nfzNocnaPomocLekarska?: boolean,
    nfzAmbulatoryjnaOpiekaSpecjalistyczna?: boolean,
    nfzRehabilitacja?: boolean,
    nfzStomatologia?: boolean,
    nfzPsychiatria?: boolean,
    nfzSzpitalnictwo?: boolean,
    nfzProgramyProfilaktyczne?: boolean,
    nfzZaopatrzenieOrtopedyczne?: boolean,
    nfzOpiekaDlugoterminowa?: boolean,
    nfzNotatki?: string,
    komercjaJest?: boolean,
    komercjaNotatki?: string,
    daneTechniczne?: string,
    autoryzacje: SecretModel[],
    autoryzacjeEx: SecretExModel[],
    kontakty: ContactDetails[]
}

type CustomerViewModel = {
    Operator: string | undefined;
    Obsluga: string | undefined;
    Rozliczenie: string | undefined;
    Dystans: string | undefined;
    Nazwa: string | undefined;
    Miejscowosc: string | undefined;
    Adres: string | undefined;
    UmowaZNFZ: boolean;
    MaFilie: boolean;
    Lekarz: boolean;
    Polozna: boolean;
    PielegniarkaSrodowiskowa: boolean;
    MedycynaSzkolna: boolean;
    TransportSanitarny: boolean;
    NocnaPomocLekarska: boolean;
    AmbulatoryjnaOpiekaSpecjalistyczna: boolean;
    Rehabilitacja: boolean;
    Stomatologia: boolean;
    Psychiatria: boolean;
    Szpitalnictwo: boolean;
    ProgramyProfilaktyczne: boolean;
    ZaopatrzenieOrtopedyczne: boolean;
    OpiekaDlugoterminowa: boolean;
    NfzNotatki: string | undefined;
    Komercja: boolean;
    KomercjaNotatki: string | undefined;
    DaneTechniczne: string | undefined;
    Kontakty: ContactDetails[],
    Autoryzacje: SecretModel[],
    AutoryzacjeEx: SecretExModel[]
}

export interface ContactDetails {
    localKey: string,
    firstName?: string,
    lastName?: string
    phoneNo?: string,
    email?: string
}

export interface SecretModel {
    localKey: string,
    location: string
    username?: string
    password?: string
    who?: string
    when?: SecretsTimestamp
}

export interface SecretExModel {
    // Used by React to identify elements during rendering of list elements
    localKey: string
    location: string
    username?: string
    password?: string
    entityName?: string
    entityCode?: string
    who?: string
    when?: SecretsTimestamp
    otpSecret?: string
    otpRecoveryKeys?: string
}

interface CustomerViewProps {
    id: EntityId
    entry: CustomerViewEntry
    itemSaved: () => void
    itemRemoved: () => void;
}

export const CustomerView: React.FC<CustomerViewProps> = props => {

    const obsluga: IComboBoxOption[] = [
        { key: 'Obsługiwany', text: 'Obsługiwany' },
        { key: 'Nie obsługiwany', text: 'Nie obsługiwany' },
        { key: 'Obsługa czasowo zawieszona', text: 'Obsługa czasowo zawieszona' },
    ];

    const rozliczenia: IComboBoxOption[] = [
        { key: 'Ryczałt', text: 'Ryczałt' },
        { key: 'Godziny', text: 'Godziny' },
    ];

    const users = useGetUsers(props.id.projectId);
    const comboBoxBasicOptions: IComboBoxOption[] = []
    users.forEach(user => {
        var userItem = { key: user, text: user };
        comboBoxBasicOptions.push(userItem);
    })

    const [model, setModel] = React.useState<CustomerViewModel>({
        Operator: props.entry.EmailOfOperator,
        Obsluga: props.entry.ModelOfSupport,
        Rozliczenie: props.entry.ModelOfBilling,
        Dystans: props.entry.Distance?.toString(),
        Nazwa: props.entry.KlientNazwa,
        Miejscowosc: props.entry.KlientMiejscowosc,
        Adres: props.entry.KlientAdres,
        UmowaZNFZ: props.entry.nfzUmowa ?? false,
        MaFilie: props.entry.nfzMaFilie ?? false,
        Lekarz: props.entry.nfzLekarz ?? false,
        Polozna: props.entry.nfzPolozna ?? false,
        PielegniarkaSrodowiskowa: props.entry.nfzPielegniarkaSrodowiskowa ?? false,
        MedycynaSzkolna: props.entry.nfzMedycynaSzkolna ?? false,
        TransportSanitarny: props.entry.nfzTransportSanitarny ?? false,
        NocnaPomocLekarska: props.entry.nfzNocnaPomocLekarska ?? false,
        AmbulatoryjnaOpiekaSpecjalistyczna: props.entry.nfzAmbulatoryjnaOpiekaSpecjalistyczna ?? false,
        Rehabilitacja: props.entry.nfzRehabilitacja ?? false,
        Stomatologia: props.entry.nfzStomatologia ?? false,
        Psychiatria: props.entry.nfzPsychiatria ?? false,
        Szpitalnictwo: props.entry.nfzSzpitalnictwo ?? false,
        ProgramyProfilaktyczne: props.entry.nfzProgramyProfilaktyczne ?? false,
        ZaopatrzenieOrtopedyczne: props.entry.nfzZaopatrzenieOrtopedyczne ?? false,
        OpiekaDlugoterminowa: props.entry.nfzOpiekaDlugoterminowa ?? false,
        NfzNotatki: props.entry.nfzNotatki,
        Komercja: props.entry.komercjaJest ?? false,
        KomercjaNotatki: props.entry.komercjaNotatki,
        DaneTechniczne: props.entry.daneTechniczne,
        Kontakty: props.entry.kontakty,
        Autoryzacje: props.entry.autoryzacje,
        AutoryzacjeEx: props.entry.autoryzacjeEx
    });

    const addContact = () => {
        const localKey = uuid();
        const newContact: ContactDetails = {
            localKey
        };
        const cloned = _.clone(model);
        cloned.Kontakty.push(newContact);
        setModel(cloned);
    }

    const onChangeBoolean = (changer: (m: CustomerViewModel, current: boolean) => void) => {
        return (ev?: React.FormEvent<HTMLElement>, checked?: boolean): void => {
            const cloned = _.clone(model);
            changer(cloned, checked ?? false);
            setModel(cloned);
        };
    }

    const onChangeText = (changer: (m: CustomerViewModel, current: string | undefined) => void) => {
        return (ev?: React.FormEvent<HTMLInputElement | HTMLTextAreaElement>, newText?: string): void => {
            const cloned = _.clone(model);
            changer(cloned, newText);
            setModel(cloned);
        };
    }

    const onChangeMemo = (changer: (m: CustomerViewModel, current: string | undefined) => void) => {
        return (ev?: React.FormEvent<HTMLInputElement | HTMLTextAreaElement>, newText?: string): void => {
            if (newText && newText?.length <= 2000) {
                const cloned = _.clone(model);
                changer(cloned, newText);
                setModel(cloned);
            }
        };
    }

    const onChangeCombo = (changer: (m: CustomerViewModel, optionKey?: string) => void) => {
        return (event: React.FormEvent<IComboBox>, option?: IComboBoxOption, index?: number, value?: string): void => {
            const cloned = _.clone(model);
            changer(cloned, option!.key as string);
            setModel(cloned);
        }
    }

    const [saveCustomerMutation, { data: saveResult, loading: saveInProgress }] = useSaveCustomerMutation();
    const [removeCustomerMutation, { data: removeResult }] = useRemoveCustomerMutation();

    if (saveResult) {
        props.itemSaved();
    }

    if (removeResult) {
        props.itemRemoved();
    }

    const [actionsDisabled, setActionsDisabled] = React.useState(false);

    const removeEndExit: MouseEventHandler<PrimaryButton> = (event): void => {
        setActionsDisabled(true);
        const { projectId, entityId, entityVersion } = props.id;
        removeCustomerMutation({
            variables: {
                projectId: props.id.projectId,
                id: { projectId, entityId, entityVersion },
            }
        });
    }

    const saveEndExit: MouseEventHandler<PrimaryButton> = (event): void => {
        setActionsDisabled(true);
        const { projectId, entityId, entityVersion } = props.id;
        const entry: CustomerInput = {
            operatorEmail: model.Operator,
            billingModel: model.Rozliczenie,
            supportStatus: model.Obsluga,
            distance: Number(model.Dystans),
            customerName: model.Nazwa ?? "Nazwa klienta",
            customerCityName: model.Miejscowosc,
            customerAddress: model.Adres,
            nfzUmowa: model.UmowaZNFZ,
            nfzMaFilie: model.MaFilie,
            nfzLekarz: model.Lekarz,
            nfzPolozna: model.Polozna,
            nfzPielegniarkaSrodowiskowa: model.PielegniarkaSrodowiskowa,
            nfzMedycynaSzkolna: model.MedycynaSzkolna,
            nfzTransportSanitarny: model.TransportSanitarny,
            nfzNocnaPomocLekarska: model.NocnaPomocLekarska,
            nfzAmbulatoryjnaOpiekaSpecjalistyczna: model.AmbulatoryjnaOpiekaSpecjalistyczna,
            nfzRehabilitacja: model.Rehabilitacja,
            nfzStomatologia: model.Stomatologia,
            nfzPsychiatria: model.Psychiatria,
            nfzSzpitalnictwo: model.Szpitalnictwo,
            nfzProgramyProfilaktyczne: model.ProgramyProfilaktyczne,
            nfzZaopatrzenieOrtopedyczne: model.ZaopatrzenieOrtopedyczne,
            nfzOpiekaDlugoterminowa: model.OpiekaDlugoterminowa,
            nfzNotatki: model.NfzNotatki,
            komercjaJest: model.Komercja,
            komercjaNotatki: model.KomercjaNotatki,
            daneTechniczne: model.DaneTechniczne
        }
        const secrets = _.chain(model.Autoryzacje)
            .map(it => {
                const ret: CustomerSecretInput = {
                    location: it.location ?? '---',
                    username: it.username,
                    password: it.password
                };
                return ret;
            })
            .value()
        const secretsEx = _.chain(model.AutoryzacjeEx)
            .map(it => {
                const ret: CustomerSecretExInput = {
                    location: it.location ?? '---',
                    username: it.username,
                    password: it.password,
                    entityName: it.entityName,
                    entityCode: it.entityCode,
                    otpSecret: it.otpSecret,
                    otpRecoveryKeys: it.otpRecoveryKeys
                };
                return ret;
            })
            .value()
        const contacts = _.chain(model.Kontakty)
            .map(it => {
                const ret: CustomerContactInput = {
                    firstName: it.firstName,
                    lastName: it.lastName,
                    phoneNo: it.phoneNo,
                    email: it.email
                };
                return ret;
            })
            .value()
        saveCustomerMutation({
            variables: {
                projectId: props.id.projectId,
                id: { projectId, entityId, entityVersion },
                entry,
                secrets,
                secretsEx,
                contacts
            },
        });
    }

    if (saveInProgress) {
        return <Spinner size={SpinnerSize.large} />
    }

    const btnStyles = {
        rootHovered: {
            backgroundColor: "#d83b01"
        }
    };

    const styles = {
         padding: 5,
         margin: 5
    };

    return (
        <Stack verticalFill>
            <Stack.Item>
                <Separator alignContent="start">Akcje: </Separator>
                <div style={styles}  >
                    <PrimaryButton text="Zapisz i wyjdź" disabled={actionsDisabled} onClick={saveEndExit} />
                    <DefaultButton text="Usuń klienta i wyjdź" disabled={actionsDisabled} styles={btnStyles} onClick={removeEndExit} />
                </div>
            </Stack.Item>
            <Stack.Item verticalFill>
                <div style={{ position: "relative", height: "100%" }}>
                    <ScrollablePane scrollbarVisibility={ScrollbarVisibility.auto}>

                        <div className="ms-Grid" dir="ltr">
                            <div className="ms-Grid-row">
                                <div className="ms-Grid-col ms-sm6 ms-md8 ms-lg10">

                                    <HorizontalSeparatorStack>
                                        <>
                                            <Separator alignContent="start">Dane ogólne: </Separator>

                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-smPush1 ms-sm4 ">
                                                    <ComboBox
                                                        label="Operator"
                                                        options={comboBoxBasicOptions}
                                                        defaultSelectedKey={model.Operator}
                                                        selectedKey={model.Operator}
                                                        onChange={onChangeCombo((m, v) => m.Operator = v)} />
                                                </div>
                                            </div>
                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-smPush1 ms-sm4 ">
                                                    <ComboBox
                                                        label="Obsługa"
                                                        options={obsluga}
                                                        defaultSelectedKey={model.Obsluga}
                                                        selectedKey={model.Obsluga}
                                                        onChange={onChangeCombo((m, v) => m.Obsluga = v)} />
                                                </div>
                                            </div>
                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-smPush1 ms-sm4 ">
                                                    <ComboBox
                                                        label="Rozliczenie"
                                                        options={rozliczenia}
                                                        defaultSelectedKey={model.Rozliczenie}
                                                        selectedKey={model.Rozliczenie}
                                                        onChange={onChangeCombo((m, v) => m.Rozliczenie = v)} />
                                                </div>
                                            </div>
                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-smPush1 ms-sm2">
                                                    <TextField
                                                        label="Dystans"
                                                        value={model.Dystans}
                                                        onChange={onChangeText((m, v) => m.Dystans = v)}
                                                        styles={narrowTextFieldStyles}
                                                    />
                                                </div>
                                            </div>

                                            <Separator alignContent="start">Dane adresowe</Separator>

                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-sm3 ms-smPush1">
                                                    <TextField label="Nazwa"
                                                        value={model.Nazwa}
                                                        placeholder="Nazwa klienta"
                                                        onChange={onChangeText((m, v) => m.Nazwa = v)} />
                                                </div>
                                            </div>
                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-sm3 ms-smPush1">
                                                    <TextField label="Miejscowość"
                                                        value={model.Miejscowosc}
                                                        placeholder="Miejscowość klienta"
                                                        onChange={onChangeText((m, v) => m.Miejscowosc = v)} />
                                                </div>
                                            </div>
                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-sm3 ms-smPush1">
                                                    <TextField label="Adres"
                                                        value={model.Adres}
                                                        placeholder="Adres klienta"
                                                        onChange={onChangeText((m, v) => m.Adres = v)} />
                                                </div>
                                            </div>

                                            <Separator alignContent="start">NFZ</Separator>

                                            <Stack horizontal tokens={stackTokens} styles={stackStyles}>
                                            </Stack>

                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-smPush1">
                                                    <Stack tokens={stackTokens}>
                                                        <Stack horizontal tokens={stackTokens}>
                                                            <Checkbox label="Umowa z NFZ"
                                                                checked={model.UmowaZNFZ}
                                                                onChange={onChangeBoolean((m, v) => m.UmowaZNFZ = v)} />
                                                            <Checkbox label="Posiada filię"
                                                                checked={model.MaFilie}
                                                                onChange={onChangeBoolean((m, v) => m.MaFilie = v)} />
                                                        </Stack>

                                                    </Stack>
                                                </div>
                                            </div>
                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-sm5 ms-smPush1">
                                                    <Checkbox label="L Lekarz" checked={model.Lekarz} onChange={onChangeBoolean((m, v) => m.Lekarz = v)} />
                                                </div>
                                                <div className="ms-Grid-col ms-sm5">
                                                    <Checkbox label="PS Pielęgniarka Środowiskowa"
                                                        checked={model.PielegniarkaSrodowiskowa}
                                                        onChange={onChangeBoolean((m, v) => m.PielegniarkaSrodowiskowa = v)} />
                                                </div>
                                            </div>
                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-sm5 ms-smPush1">
                                                    <Checkbox label="O Położna"
                                                        checked={model.Polozna}
                                                        onChange={onChangeBoolean((m, v) => m.Polozna = v)} />
                                                </div>
                                                <div className="ms-Grid-col ms-sm5">
                                                    <Checkbox label="MPSZ Medycyna Szkolna"
                                                        checked={model.MedycynaSzkolna}
                                                        onChange={onChangeBoolean((m, v) => m.MedycynaSzkolna = v)} />
                                                </div>
                                            </div>
                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-sm5 ms-smPush1">
                                                    <Checkbox label="Transport Sanitarny"
                                                        checked={model.TransportSanitarny}
                                                        onChange={onChangeBoolean((m, v) => m.TransportSanitarny = v)} />
                                                </div>
                                                <div className="ms-Grid-col ms-sm5">
                                                    <Checkbox label="NPL Nocna pomoc lekarska"
                                                        checked={model.NocnaPomocLekarska}
                                                        onChange={onChangeBoolean((m, v) => m.NocnaPomocLekarska = v)} />
                                                </div>
                                            </div>
                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-sm5 ms-smPush1">
                                                    <Checkbox label="AOS Ambulatoryjna opieka specjalistyczna"
                                                        checked={model.AmbulatoryjnaOpiekaSpecjalistyczna}
                                                        onChange={onChangeBoolean((m, v) => m.AmbulatoryjnaOpiekaSpecjalistyczna = v)} />
                                                </div>
                                                <div className="ms-Grid-col ms-sm5">
                                                    <Checkbox label="REH Rehabilitacja"
                                                        checked={model.Rehabilitacja}
                                                        onChange={onChangeBoolean((m, v) => m.Rehabilitacja = v)} />
                                                </div>
                                            </div>
                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-sm5 ms-smPush1">
                                                    <Checkbox label="STM Stomatologia"
                                                        checked={model.Stomatologia}
                                                        onChange={onChangeBoolean((m, v) => m.Stomatologia = v)} />
                                                </div>
                                                <div className="ms-Grid-col ms-sm5">
                                                    <Checkbox label="PSY Psychiatria"
                                                        checked={model.Psychiatria}
                                                        onChange={onChangeBoolean((m, v) => m.Psychiatria = v)} />
                                                </div>
                                            </div>
                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-sm5 ms-smPush1">
                                                    <Checkbox label="SZP Szpitalnictwo"
                                                        checked={model.Szpitalnictwo}
                                                        onChange={onChangeBoolean((m, v) => m.Szpitalnictwo = v)} />
                                                </div>
                                                <div className="ms-Grid-col ms-sm5">
                                                    <Checkbox label="PROF Programy profilaktyczne"
                                                        checked={model.ProgramyProfilaktyczne}
                                                        onChange={onChangeBoolean((m, v) => m.ProgramyProfilaktyczne = v)} />
                                                </div>
                                            </div>
                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-sm5 ms-smPush1">
                                                    <Checkbox label="ZOP Zaopatrzenie ortopedyczne i pomocniczne"
                                                        checked={model.ZaopatrzenieOrtopedyczne}
                                                        onChange={onChangeBoolean((m, v) => m.ZaopatrzenieOrtopedyczne = v)} />
                                                </div>
                                                <div className="ms-Grid-col ms-sm5">
                                                    <Checkbox label="OPD Opieka długoterminowa"
                                                        checked={model.OpiekaDlugoterminowa}
                                                        onChange={onChangeBoolean((m, v) => m.OpiekaDlugoterminowa = v)} />
                                                </div>
                                            </div>
                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-sm6 ms-smPush1">
                                                    <TextField
                                                        multiline={true}
                                                        value={model.NfzNotatki}
                                                        placeholder="Notatki NFZ"
                                                        onChange={onChangeMemo((m, v) => m.NfzNotatki = v)}
                                                    />
                                                </div>
                                            </div>


                                            <Separator alignContent="start">Komercja</Separator>
                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-sm5 ms-smPush1">
                                                    <Checkbox label="Komercja"
                                                        checked={model.Komercja}
                                                        onChange={onChangeBoolean((m, v) => m.Komercja = v)} />
                                                </div>
                                            </div>
                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-sm6 ms-smPush1">
                                                    <TextField
                                                        multiline={true}
                                                        placeholder="Dane opisowe"
                                                        value={model.KomercjaNotatki}
                                                        onChange={onChangeMemo((m, v) => m.KomercjaNotatki = v)}
                                                    />
                                                </div>
                                            </div>
                                        </>
                                        <>
                                            <Separator alignContent="start">Autoryzacje</Separator>

                                            {model.AutoryzacjeEx
                                                .map(item => <UserPasswordItemExt
                                                    model={item}
                                                    changedBy={item.who ?? "-"}
                                                    changedWhen={item.when?.value ?? "-"}
                                                    onChange={v => {
                                                        const clone = _.clone(model);
                                                        const index = _.findIndex(clone.AutoryzacjeEx, it => it.localKey === v.localKey);
                                                        // update new values and clone read-only values
                                                        const readOnlyValues = { };
                                                        const newValue = { ...v, ...readOnlyValues}
                                                        clone.AutoryzacjeEx[index] = newValue ;
                                                        setModel(clone);
                                                    }}
                                                    onRemove={localKey => {
                                                        const clone = _.clone(model);
                                                        const index = _.findIndex(clone.AutoryzacjeEx, it => it.localKey === localKey);
                                                        clone.AutoryzacjeEx.splice(index, 1);
                                                        setModel(clone);
                                                    }}
                                                />)}

                                            {model.Autoryzacje
                                                .map(item => <UserPasswordItem
                                                    model={item}
                                                    changedBy={item.who ?? "-"}
                                                    changedWhen={item.when?.value ?? "-"}
                                                    onChange={v => {
                                                        const clone = _.clone(model);
                                                        const index = _.findIndex(clone.Autoryzacje, it => it.localKey === v.localKey);
                                                        clone.Autoryzacje[index] = v;
                                                        setModel(clone);
                                                    }}
                                                    onRemove={localKey => {
                                                        const clone = _.clone(model);
                                                        const index = _.findIndex(clone.Autoryzacje, it => it.localKey === localKey);
                                                        clone.Autoryzacje.splice(index, 1);
                                                        setModel(clone);
                                                    }}
                                                />)}

                                            <NewSecret
                                                newAuthorisationExRequested={name => {
                                                    const clone = _.clone(model);
                                                    const localKey = uuid();
                                                    const newItem: SecretExModel = {
                                                        localKey,
                                                        location: name
                                                    };
                                                    clone.AutoryzacjeEx.push(newItem);
                                                    setModel(clone);
                                                }}
                                                newAuthorisationRequested={name => {
                                                    const clone = _.clone(model);
                                                    const localKey = uuid();
                                                    const newItem: SecretModel = {
                                                        localKey,
                                                        location: name
                                                    };
                                                    clone.Autoryzacje.push(newItem);
                                                    setModel(clone);
                                                }} />
                                        </>
                                        <>
                                            <Separator alignContent="start">Dane kontaktowe</Separator>

                                            {model.Kontakty
                                                .map(item => <NewContactItem
                                                    model={item}
                                                    onChange={v => {
                                                        const clone = _.clone(model);
                                                        const index = _.findIndex(clone.Kontakty, it => it.localKey === v.localKey);
                                                        clone.Kontakty[index] = v;
                                                        setModel(clone);
                                                    }}
                                                    onRemove={localKey => {
                                                        const clone = _.clone(model);
                                                        const index = _.findIndex(clone.Kontakty, it => it.localKey === localKey);
                                                        clone.Kontakty.splice(index, 1);
                                                        setModel(clone);
                                                    }}
                                                />)}

                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-smPush1 ms-sm4">
                                                    <DefaultButton onClick={addContact}>Dodaj nowy kontakt</DefaultButton>
                                                </div>
                                            </div>
                                        </>
                                        <>
                                            <Separator alignContent="start">Dane techniczne</Separator>
                                            <div className="ms-Grid-row">
                                                <div className="ms-Grid-col ms-sm6 ms-smPush1">
                                                    <TextField
                                                        multiline={true}
                                                        placeholder="Adresy IP serwerów, inne"
                                                        value={model.DaneTechniczne}
                                                        onChange={onChangeMemo((m, v) => m.DaneTechniczne = v)} />
                                                </div>
                                            </div>
                                        </>
                                    </HorizontalSeparatorStack>
                                </div>
                            </div>
                        </div>
                    </ScrollablePane>
                </div>

            </Stack.Item>
        </Stack>
    );
}
