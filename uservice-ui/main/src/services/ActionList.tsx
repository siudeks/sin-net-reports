import { DetailsList, DetailsListLayoutMode, IColumn, IDetailsColumnRenderTooltipProps, IDetailsHeaderProps, IRenderFunction, IStackTokens, mergeStyleSets, ScrollablePane, ScrollbarVisibility, SelectionMode, Stack, Sticky, StickyPositionType, TextField, Toggle, TooltipHost } from "@fluentui/react";
import _ from "lodash";
import * as React from "react";
import { useState } from "react";
import { connect, ConnectedProps } from "react-redux";
import { Link } from "react-router-dom";
import { Dispatch } from "redux";
import { toActionModel } from "../api/DtoMapper";
import { asDtoDates } from "../api/Mapper";
import { LocalDateView } from "../app/LocalDateView";
import { useFetchServicesQuery } from "../Components/.generated/components";
import { ServiceAppModel } from "../store/actions/ServiceModel";
import { RootState } from "../store/reducers";
import { TimePeriod } from "../store/viewcontext/TimePeriod";
import { ActionEditItem, VIEWCONTEXT_ACTION_EDIT_START } from "../store/viewcontext/types";
import { Duration } from "./ActionList.Duration";
import { ServiceListModel } from "./ServiceListModel";

const classNames = mergeStyleSets({
  fileIconHeaderIcon: {
    padding: 0,
    fontSize: "16px"
  },
  fileIconCell: {
    textAlign: "center",
    selectors: {
      "&:before": {
        content: ".",
        display: "inline-block",
        verticalAlign: "middle",
        height: "100%",
        width: "0px",
        visibility: "hidden"
      }
    }
  },
  fileIconImg: {
    verticalAlign: "middle",
    maxHeight: "16px",
    maxWidth: "16px"
  },
  controlWrapper: {
    display: "flex",
    flexWrap: "wrap"
  },
  exampleToggle: {
    display: "inline-block",
    marginBottom: "10px",
    marginRight: "30px"
  },
  selectionDetails: {
    marginBottom: "20px"
  }
});
const controlStyles = {
  root: {
    margin: "0 30px 20px 0",
    maxWidth: "300px"
  }
};

export interface IContentState {
  columns: IColumn[];
  items: IDocument[];
  isModalSelection: boolean;
  announcedMessage?: string;
}

export interface IDocument extends ServiceListModel {
}

export interface TypedColumn extends IColumn {
  fieldName: keyof IDocument;
}

export interface ContentProps {
}

const mapStateToProps = (state: RootState) => {
  if (state.appState.empty) {
    throw new Error('Invalid state');
  }
  return { ...state.viewContext, selectedProjectId: state.appState.projectId, currentEmail: state.auth.email };
};
const mapDispatchToProps = (dispatch: Dispatch) => {
  return {
    editItem: (actionEntityId: string) => {
      var action: ActionEditItem = {
        type: VIEWCONTEXT_ACTION_EDIT_START,
        payload: { actionEntityId }
      }
      dispatch(action);
    }
  }
}
const connector = connect(mapStateToProps, mapDispatchToProps);


type PropsFromRedux = ConnectedProps<typeof connector>

const ConnectedContent: React.FC<PropsFromRedux> = props => {

  const [dateSorted, setDateSorted] = useState(true);
  const [dateSortedDescending, setDateSortedDescending] = useState(true);

  const initialColumns: TypedColumn[] = [
    {
      key: "column4", name: "Pracownik", fieldName: "servicemanEmail", minWidth: 70, maxWidth: 90, isResizable: true, isCollapsible: true, data: "string",
      onRender: (item: IDocument) => {
        return <Link to={`/actions/${item.projectId}/${item.entityId}/${item.entityVersion}`}>{item.servicemanEmail}</Link>;
      },
      isPadded: true
    },
    {
      key: "column3", name: "Data", fieldName: "when", minWidth: 70, maxWidth: 90, isResizable: true,
      data: "date",
      isSorted: dateSorted,
      isSortedDescending: dateSortedDescending,
      onColumnClick: () => { setDateSortedDescending(!dateSortedDescending) },
      onRender: (item: IDocument) => {
        return <LocalDateView item={item.when} />
      },
      isPadded: true
    },
    {
      key: "column2", name: "Klient", fieldName: "customerName", minWidth: 210, maxWidth: 350, isRowHeader: true, isResizable: true,
      data: "string",
      isPadded: true
    },
    {
      key: "column6", name: "Usługa", fieldName: "description", minWidth: 300, maxWidth: 300, isResizable: true, isCollapsible: true,
      data: "number",
    },
    {
      key: "column5", name: "Czas", fieldName: "duration", minWidth: 70, maxWidth: 90, isResizable: true, isCollapsible: true, data: "number",
      onRender: (item: IDocument) => <Duration duration={item.duration} />
    },
    {
      key: "column7", name: "Dojazd", fieldName: "distance", minWidth: 70, maxWidth: 90, isResizable: true, isCollapsible: true, data: "number",
      onRender: (item: IDocument) => {
        return <span>{item.distance}</span>;
      }
    }
  ];

  const [lastTouchedActionId, setlastTouchedActionId] = useState(props.lastTouchedActionId);

  const newDataIsComing = props.lastTouchedActionId != null && props.lastTouchedActionId !== lastTouchedActionId;
  if (newDataIsComing) {
    console.log('newDataIsComing');
    setlastTouchedActionId(props.lastTouchedActionId);
  }

  const periodDto = asDtoDates(props.period);
  const { data, refetch } = useFetchServicesQuery({
    variables: {
      projectId: props.selectedProjectId,
      from: periodDto.dateFrom,
      to: periodDto.dateTo
    }
  });
  if (newDataIsComing) {
    refetch();
  }

  const [onlyMyData, setOnlyMyData] = useState(true);
  const filterByOnlyMyData = (item: ServiceListModel): boolean => {
    if (!onlyMyData) return true;
    if (item.servicemanEmail === props.currentEmail) return true;
    return false;
  }

  const [onlyDay, setOnlyDay] = useState<string | undefined>();
  const filterByOnlyDay = (item: ServiceListModel): boolean => {
    if (!onlyDay) return true;
    if (item.when.day.toString().includes(onlyDay)) return true;
    return false;
  }

  const [onlyCustomer, setOnlyCustomer] = useState<string | undefined>();
  const filterByOnlyCustomer = (item: ServiceListModel): boolean => {
    if (!onlyCustomer) return true;
    if (!item.customerName) return false;
    if (item.customerName?.toLowerCase().includes(onlyCustomer.toLowerCase())) return true;
    return false;
  }

  var items: ServiceListModel[] = _.chain(data?.Actions.search.items)
    .map(it => toActionModel(it))
    .map(it => toLocalModel(it))
    .filter(it => filterByOnlyMyData(it))
    .filter(it => filterByOnlyDay(it))
    .filter(it => filterByOnlyCustomer(it))
    .value();
  items.sort((i1, i2) => ('' + i1.customerName).localeCompare('' + i2.customerName));
  if (!dateSortedDescending) items.sort((i1, i2) => i1.when.day - i2.when.day);
  if (dateSortedDescending) items.sort((i1, i2) => i2.when.day - i1.when.day);

  const fold = (reducer: (acc: number, it: number) => number, init: number, xs: number[]) => {
    let acc = init;
    for (const x of xs) {
      acc = reducer(acc, x);
    }
    return acc;
  };
  const totalTime = () => {
    const duration = fold((acc, it) => acc + it, 0, items.map(it => it.duration ?? 0));
    var hours = Math.floor(duration / 60);
    var minutes = duration - hours * 60;
    return hours + ':' + ('00' + minutes).substr(-2);
  };

  const stackTokens: IStackTokens = { padding: "10px" };



  const [currentPeriod, setCurrentPeriod] = useState<TimePeriod | null>(null);
  if (currentPeriod !== props.period) {
    setCurrentPeriod(props.period);
  }


  const onRenderDetailsHeader: IRenderFunction<IDetailsHeaderProps> = (props, defaultRender) => {
    if (!props) {
      return null;
    }
    const onRenderColumnHeaderTooltip: IRenderFunction<IDetailsColumnRenderTooltipProps> = tooltipHostProps => (
      <TooltipHost {...tooltipHostProps} />
    );
    return (
      <Sticky stickyPosition={StickyPositionType.Header} isScrollSynced>
        {defaultRender!({
          ...props,
          onRenderColumnHeaderTooltip,
        })}
      </Sticky>
    );
  };



  return (
    <Stack verticalFill>
      <Stack.Item>
        <Stack horizontal tokens={stackTokens}>
            <Toggle
              label="Tylko moje dane"
              checked={onlyMyData}
              onChange={(e, v) => setOnlyMyData(v ?? true)}
              onText="Tylko moje"
              offText="Wszystkie"
              styles={controlStyles}
            />
            <TextField label="Tylko dzień:" value={onlyDay} onChange={(e, v) => setOnlyDay(v)} styles={controlStyles} />
            <TextField label="Kontrahent:" styles={controlStyles} value={onlyCustomer} onChange={(e, v) => setOnlyCustomer(v)} />
            <TextField borderless readOnly label="Suma godzin" value={totalTime()} />
        </Stack>
      </Stack.Item>
      <Stack.Item verticalFill>
        <div style={{ position: "relative", height: "100%" }}>
          <ScrollablePane scrollbarVisibility={ScrollbarVisibility.auto}>
            <DetailsList
              onRenderDetailsHeader={onRenderDetailsHeader}
              items={items}
              compact={true}
              columns={initialColumns}
              selectionMode={SelectionMode.none}
              setKey="none"
              layoutMode={DetailsListLayoutMode.justified}
              isHeaderVisible={true}
            />
          </ScrollablePane>
        </div>
      </Stack.Item>
    </Stack>

  );
}

export const Content = connector(ConnectedContent);

const toLocalModel = (it: ServiceAppModel): ServiceListModel => {
  return {
    projectId: it.projectId,
    entityId: it.entityId,
    entityVersion: it.entityVersion,
    servicemanEmail: it.servicemanEmail,
    description: it.description,
    when: it.when,
    duration: it.duration,
    distance: it.distance,
    customerName: it.customer?.name
  }
}
