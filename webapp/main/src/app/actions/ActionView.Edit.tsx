import { EntityId, ServiceAppModel } from "../../store/actions/ServiceModel";
import { RootState } from "../../store/reducers";
import { Dispatch } from "redux";
import { connect, ConnectedProps } from "react-redux";
import _ from "lodash";
import { ComboBox, DateRangeType, DefaultButton, IComboBox, IComboBoxOption, PrimaryButton, Stack, TextField } from "office-ui-fabric-react";
import React, { useCallback, useEffect, useRef, useState } from "react";
import { AppDatePicker } from "../../services/ActionList.DatePicker";
import { LocalDate } from "../../store/viewcontext/TimePeriod";
import { useGetUsers } from "../../api/useGetUsers";
import { useListCustomersQuery, useRemoveActionMutation, useUpdateActionMutation } from "../../Components/.generated/components";
import { dates } from "../../api/DtoMapper";

const mapStateToProps = (state: RootState) => {
    if (state.appState.empty) {
        throw 'Invalid state';
    }
    return state;
}

const mapDispatchToProps = (dispatch: Dispatch) => {
    return {}
}

const connector = connect(mapStateToProps, mapDispatchToProps);
type PropsFromRedux = ConnectedProps<typeof connector>;


interface ActionViewEditProps extends PropsFromRedux {
    item: ServiceAppModel,
    cancelEdit: () => void,
    actionUpdated: (entity: EntityId) => void
}

const ActionViewEditLocal: React.FC<ActionViewEditProps> = props => {

    const { item, cancelEdit, actionUpdated } = props;

    const propsProjectId = item?.projectId;
    const propsEntityId = item?.entityId;
    const propsEntityVersion = item?.entityVersion;
    const versionedProps = [propsEntityId, propsEntityVersion, propsProjectId];

    const [projectId, setProjectId] = useState(propsProjectId);
    useEffect(() => {
        setEntityId(propsProjectId);
    }, versionedProps);

    const [entityId, setEntityId] = useState(propsEntityId);
    useEffect(() => {
        setEntityId(propsEntityId);
    }, versionedProps);

    const [entityVersion, setEntityVersion] = useState(propsEntityVersion);
    useEffect(() => {
        setEntityVersion(propsEntityVersion);
    }, versionedProps);

    console.log(`edit : ${propsEntityId}:${propsEntityVersion}`);

    const defaultServicemanName = item?.servicemanName;
    const [servicemanName, setServicemanName] = useState(defaultServicemanName);
    const onChangeServicemanName = useCallback(
        (ev: React.FormEvent<IComboBox>, option?: IComboBoxOption) => {
            const a = option?.key as string;
            setServicemanName(a);
        },
        [setServicemanName],
    );

    const initialValue = item?.when;
    const [actionDate, setActionDate] = useState(initialValue);
    useEffect(() => {
        setActionDate(initialValue);
    }, versionedProps);
    const onChangeDate = useCallback(
        (newValue: LocalDate) => {
            setActionDate(newValue);
        },
        versionedProps,
    );

    const defaultCustomerName = item?.customerName;
    const [customerName, setCustomerName] = useState(defaultCustomerName);
    // useEffect(() => {
    //   setCustomerName(defaultCustomerName);
    // }, [defaultCustomerName]);
    const onChangeCustomerName = useCallback(
        (ev: React.FormEvent<IComboBox>, option?: IComboBoxOption) => {
            const a = option?.key as string;
            setCustomerName(a);
        },
        [setCustomerName],
    );

    const propsDescription = item?.description;
    const [description, setDescription] = useState(propsDescription);
    const [descriptionError, setDescriptionError] = useState('');
    useEffect(() => {
        setDescription(propsDescription)
    }, [propsDescription]);
    const onChangeDescription = useCallback(
        (event: React.FormEvent<HTMLInputElement | HTMLTextAreaElement>, newValue?: string) => {
            var value = newValue ?? '';
            setDescription(value);
            var errorMessage = value.length > 4000
                ? 'Za długi opis'
                : '';
            if (errorMessage != descriptionError) setDescriptionError(errorMessage);
        },
        [],
    );



    const [durationAsText, setDurationAsText] = useState("0:00");
    const durationRef = useRef(0);
    const [durationError, setDurationError] = useState("");
    useEffect(() => {
        const duration = props.item.duration ?? 0;
        var hours = Math.floor(duration / 60);
        var minutes = duration - hours * 60;
        setDurationAsText(hours + ':' + ('00' + minutes).substr(-2));
        durationRef.current = duration;
    }, versionedProps);
    const onChangeDuration = useCallback(
        (event: React.FormEvent<HTMLInputElement | HTMLTextAreaElement>, newValue?: string) => {
            const newDurationAsText = newValue ?? "0:00";
            const [hoursAsText, minutesAsText] = newDurationAsText.split(":")
            const hours = Number(hoursAsText);
            const minutes = Number(minutesAsText);
            const newDuration = hours * 60 + minutes;

            setDurationAsText(newDurationAsText);
            if (isNaN(newDuration)) {
                setDurationError(`Nieprawidłowy czas: ${newDurationAsText}`)
            } else {
                durationRef.current = newDuration;
                setDurationError("");
            }
        },
        versionedProps,
    );

    const propsDistance = "" + item?.distance;
    const [distance, setDistance] = useState(propsDistance);
    useEffect(() => {
        setDistance(propsDistance)
    }, versionedProps);
    const onChangeDistance = useCallback(
        (event: React.FormEvent<HTMLInputElement | HTMLTextAreaElement>, newValue?: string) => {
            setDistance(newValue ?? "0");
        },
        [],
    );

    const stackTokens = { childrenGap: 8 }

    const users = useGetUsers(projectId);

    const comboBoxBasicOptions = _.chain(users)
        .map(it => ({ key: it, text: it }))
        .value();

    const [updateActionMutation, { loading: updateActionInProgress, data: data2 }] = useUpdateActionMutation();
    if (data2) {
        actionUpdated({
            projectId: propsProjectId,
            entityId: propsEntityId,
            entityVersion: propsEntityVersion
        });
        cancelEdit();
    }

    const [removeActionMutation, { loading: removeActionInProgress, data: data3 }] = useRemoveActionMutation();
    if (data3) {
        actionUpdated({
            projectId: propsProjectId,
            entityId: propsEntityId,
            entityVersion: propsEntityVersion
        });
        cancelEdit();
    }

    const removeAndExit = () => {
        removeActionMutation({
            variables: {
                projectId,
                entityId,
                entityVersion
            }
        })
    }

    const updateAction = () => {
        const dtoDate = dates(actionDate).slashed;
        updateActionMutation({
            variables: {
                projectId,
                entityId,
                entityVersion,
                distance: Number(distance),
                duration: durationRef.current,
                what: description,
                when: dtoDate,
                who: servicemanName,
                whom: customerName
            }
        })
    };

    const { data } = useListCustomersQuery({
        variables: {
            projectId
        }
    })

    const customerOptions = _.chain(data?.Customers.list)
        .map(it => ({ key: it.data.customerName, text: it.data.customerName }))
        .value();

    const btnStyles = {
        rootHovered: {
            backgroundColor: "#d83b01"
        }
    };


    return (
        <>
            <Stack tokens={stackTokens}>
                <div className="ms-Grid-row">
                    <div className="ms-Grid-col ms-sm4">
                        <ComboBox label="Pracownik" selectedKey={servicemanName} options={comboBoxBasicOptions} autoComplete="on" onChange={onChangeServicemanName}
                        />
                    </div>
                    <div className="ms-Grid-col ms-sm2">
                        <AppDatePicker
                            onSelectDate={value => onChangeDate(value)}
                            current={actionDate}
                            dateRangeType={DateRangeType.Day} autoNavigateOnSelection={true} showGoToToday={true} />
                    </div>
                </div>

                <div className="ms-Grid-row">
                    <div className="ms-Grid-col ms-sm4">
                        <ComboBox label="Klient" selectedKey={customerName} options={customerOptions} autoComplete="on" onChange={onChangeCustomerName}
                        />
                    </div>
                    <div className="ms-Grid-col ms-sm6">
                        <TextField label="Usługa" multiline={true} value={description} errorMessage={descriptionError} onChange={onChangeDescription}
                        />
                    </div>
                </div>

                <div className="ms-Grid-row">
                    <div className="ms-Grid-col ms-sm4">
                        <TextField label="Czas" placeholder="0:00" value={durationAsText} errorMessage={durationError} onChange={onChangeDuration} />
                    </div>
                    <div className="ms-Grid-col ms-sm2">
                        <TextField label="Dojazd" value={distance} onChange={onChangeDistance}
                        />
                    </div>
                </div>

                <div className="ms-Grid-row">
                    <div className="ms-Grid-col ms-sm12">
                        <Stack horizontal tokens={stackTokens}>
                            <PrimaryButton disabled={updateActionInProgress} text="Aktualizuj"
                                onClick={() => {
                                    updateAction();
                                    // cancelEdit();
                                }} />
                            <DefaultButton onClick={() => cancelEdit()} text="Anuluj" />
                            <DefaultButton text="Usuń i wyjdź" disabled={updateActionInProgress} styles={btnStyles} onClick={removeAndExit} />
                        </Stack>
                    </div>
                </div>
            </Stack>
        </>

    );
}

export const ActionViewEdit = connect(mapStateToProps, mapDispatchToProps)(ActionViewEditLocal);

