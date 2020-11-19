import _ from "lodash";
import { ComboBox, DateRangeType, DefaultButton, FocusTrapZone, IComboBox, IComboBoxOption, IStackStyles, MaskedTextField, memoizeFunction, PrimaryButton, Stack, TextField } from "office-ui-fabric-react";
import React, { useCallback, useEffect, useRef, useState } from "react";
import { dates } from "../api/DtoMapper";
import { useGetUsersQuery, useUpdateActionMutation } from "../Components/.generated/components";
import { AppDatePicker } from "./ActionList.DatePicker";
import { EntityId, ServiceAppModel } from "../store/actions/ServiceModel";
import { LocalDate, TimePeriod } from "../store/viewcontext/TimePeriod";
import { asDtoDate } from "../api/Mapper";
import { useGetUsers } from "../api/useGetUsers";

interface EditActionSomeProps {
  item: ServiceAppModel,
  cancelEdit: () => void,
  actionUpdated: (entity: EntityId) => void
}

/**
 * Place to take decision what state visualisation should be displayed based on give action id.
 * @param props 
 */
export const EditActionSome: React.FC<EditActionSomeProps> = props => {

  const { item, cancelEdit, actionUpdated } = props;

  const propsEntityId = item?.entityId;
  const propsEntityVersion = item?.entityVersion;
  const versionedProps = [propsEntityId, propsEntityVersion];

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
  // useEffect(() => {
  //   setServicemanName(defaultServicemanName);
  // }, [defaultServicemanName]);
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
    (event: React.FormEvent<HTMLInputElement | HTMLTextAreaElement>, newValue?: string) => {
      setCustomerName(newValue || '');
    },
    [],
  );

  const propsDescription = item?.description;
  const [description, setDescription] = useState(propsDescription);
  useEffect(() => {
    setDescription(propsDescription)
  }, [propsDescription]);
  const onChangeDescription = useCallback(
    (event: React.FormEvent<HTMLInputElement | HTMLTextAreaElement>, newValue?: string) => {
      setDescription(newValue || '');
    },
    [],
  );



  const [durationAsText, setDurationAsText] = useState("0:00");
  const durationRef = useRef(0);
  const [durationError, setDurationError] = useState("");
  useEffect(() => {
    const { duration } = props.item;
    var hours = Math.floor(duration / 60);
    var minutes = duration - hours * 60;
    setDurationAsText(hours + ('00' + minutes).substr(-2));
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

  const [disabledFocusTrap, setDisabledFocusTrap] = useState(false);
  // useEffect(() => {
  //   setDisabledFocusTrap(false);
  // }, [item.entityId]);

  const getStackStyles = memoizeFunction(
    (useTrapZone: boolean): Partial<IStackStyles> => ({
      root: { border: `2px solid ${useTrapZone ? '#ababab' : 'transparent'}`, padding: 10 }
    })
  );
  const stackTokens = { childrenGap: 8 }

  const users = useGetUsers();
  
  const comboBoxBasicOptions = _.chain(users)
    .map(it => ({ key: it, text: it }))
    .value();

  const [updateActionMutation, { loading: updateActionInProgress, data: data2 }] = useUpdateActionMutation();
  if (data2) {
    actionUpdated({
      entityId: propsEntityId,
      entityVersion: propsEntityVersion
    });
    cancelEdit();
  }

  const updateAction = () => {
    const dtoDate = dates(actionDate).slashed;
    updateActionMutation({
      variables: {
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

  return (
    <>
      <FocusTrapZone disabled={disabledFocusTrap}>
        <Stack tokens={stackTokens} styles={getStackStyles(!disabledFocusTrap)}>
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
              <TextField label="Klient" value={customerName} onChange={onChangeCustomerName}
              />
            </div>
            <div className="ms-Grid-col ms-sm6">
              <TextField label="Usługa" value={description} onChange={onChangeDescription}
              />
            </div>
          </div>

          <div className="ms-Grid-row">
            <div className="ms-Grid-col ms-sm4">
              <MaskedTextField label="Czas" mask="9:99" value={durationAsText} errorMessage={durationError} onChange={onChangeDuration} />
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
              </Stack>
            </div>
          </div>
        </Stack>
      </FocusTrapZone>
    </>

  );
}
