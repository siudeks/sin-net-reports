import * as React from 'react';
import { CommandBar, ICommandBarItemProps, IButtonProps, IContextualMenuItemProps, ContextualMenuItem, IContextualMenuStyles, getTheme, IContextualMenuItemStyles } from "@fluentui/react";
import { Dispatch } from 'redux';
import { connect, ConnectedProps } from 'react-redux';
import { RootState } from '../store/reducers';
import { previousPeriodCommand, nextPeriodCommand } from '../store/viewcontext/actions';
import { useNewActionMutation } from '../Components/.generated/components';
import { asDtoDate } from '../api/Mapper';
import { EntityId } from '../store/actions/ServiceModel';
import { ActionEditUpdated, VIEWCONTEXT_ACTION_EDIT_UPDATED } from '../store/viewcontext/types';
import { LocalDate } from '../store/viewcontext/TimePeriod';
import { Redirect } from 'react-router-dom';

const overflowProps: IButtonProps = { ariaLabel: 'More commands' };

const mapStateToProps = (state: RootState) => {
  if (state.appState.empty) {
    throw new Error('Invalid state');
  }

  return {
    viewContext: state.viewContext,
    session: state.auth,
    selectedProjectId: state.appState.projectId
  };
};
const mapDispatchToProps = (dispatch: Dispatch) => {
  return {
    onPreviousMonthRequested: () => {
      dispatch(previousPeriodCommand())
    },
    onNextMonthRequested: () => {
      dispatch(nextPeriodCommand())
    },
    actionUpdated: (item: EntityId) => {
      var action: ActionEditUpdated = {
        type: VIEWCONTEXT_ACTION_EDIT_UPDATED,
        payload: {
          lastTouchedEntityId: item
        }
      }
      dispatch(action);
    }
  }
}

const connector = connect(mapStateToProps, mapDispatchToProps);
type PropsFromRedux = ConnectedProps<typeof connector>

interface ServiceCommandBarProps extends PropsFromRedux {
  onPreviousMonthRequested: () => void;
  onNextMonthRequested: () => void;
  onReportsViewRequested: () => void;
}

enum WaitingState {
  NOT_STARTED,
  IN_PROGRESS,
  FINISHED
}
const ServiceCommandBarView: React.FC<ServiceCommandBarProps> = (props) => {
  const { viewContext } = props;
  const [newServiceMutation] = useNewActionMutation();
  const [waiting, setWaiting] = React.useState(WaitingState.NOT_STARTED);
  const [waitingResult, setWaitingResult] = React.useState<EntityId | undefined>();

  if (waiting === WaitingState.IN_PROGRESS) {
    return <div>Czekanie na stworzenie wpisu usługi ...</div>
  }

  if (waiting === WaitingState.FINISHED) {
    return <Redirect
      from={'/actions/'}
      to={`/actions/${waitingResult?.projectId}/${waitingResult?.entityId}/${waitingResult?.entityVersion}`} />
  }
  const newService = () => {
    const currentYear = new Date().getFullYear();
    const currentMonth = new Date().getMonth() + 1;
    const { year: requestedYear, month: requestedMonth } = viewContext.period.getValue().dateFrom;

    let date: LocalDate;
    if (currentYear === requestedYear && currentMonth === requestedMonth) {
      date = {
        year: currentYear,
        month: currentMonth,
        day: new Date().getDate()
      }
    } else {
      date = viewContext.period.getValue().dateFrom
    }
    const dateFrom = asDtoDate(date);
    setWaiting(WaitingState.IN_PROGRESS);
    newServiceMutation({
      variables: {
        projectId: props.selectedProjectId,
        when: dateFrom
      }
    }).then(r => {
      var createdEntityId = r.data?.Actions.newAction;
      if (!createdEntityId) return;
      setWaitingResult(createdEntityId);
      setWaiting(WaitingState.FINISHED);
      props.actionUpdated(createdEntityId);
    })
  }


  const theme = getTheme();
  // Styles for both command bar and overflow/menu items
  const itemStyles: Partial<IContextualMenuItemStyles> = {
    label: { fontSize: 18 },
    icon: { color: theme.palette.red },
    iconHovered: { color: theme.palette.redDark },
  };
  // For passing the styles through to the context menus
  const menuStyles: Partial<IContextualMenuStyles> = {
    subComponentStyles: { menuItem: itemStyles, callout: {} },
  };

  // Custom renderer for menu items (these must have a separate custom renderer because it's unlikely
  // that the same component could be rendered properly as both a command bar item and menu item).
  // It's also okay to custom render only the command bar items without changing the menu items.
  const CustomMenuItem: React.FunctionComponent<IContextualMenuItemProps> = props => {
    // Due to ContextualMenu implementation quirks, passing styles or onClick here doesn't work.
    // The onClick handler must be on the ICommandBarItemProps item instead (_overflowItems in this example).
    return <ContextualMenuItem {...props} />;
  };


  const _items: ICommandBarItemProps[] = [
    {
      key: 'newService',
      text: 'Nowa usługa',
      split: true,
      iconProps: { iconName: 'Add' },
      onClick: () => newService()
    },
    {
      key: 'prevMonth',
      text: 'Poprzedni miesiąc',
      split: true,
      iconProps: { iconName: 'Previous' },
      onClick: props.onPreviousMonthRequested
    },
    {
      key: 'nextMonth',
      text: 'Następny miesiąc',
      split: true,
      iconProps: { iconName: 'Next' },
      onClick: props.onNextMonthRequested
    },
    {
      key: 'navigateToReports',
      text: 'Raporty',
      split: true,
      iconProps: { iconName: 'ZipFolder' },
      onClick: props.onReportsViewRequested
    },

  ];

  return (
    <div>
      <CommandBar
        items={_items}
        overflowButtonProps={overflowProps}
      />
    </div>
  );
};


export const ServiceCommandBar = connect(mapStateToProps, mapDispatchToProps)(ServiceCommandBarView);