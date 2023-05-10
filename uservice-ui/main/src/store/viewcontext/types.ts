import { Action } from "redux";
import { EntityId } from "../actions/ServiceModel";
import { TimePeriod } from "./TimePeriod";

/**
 * We decided to define shared UI context as a single intended state so that all componenst has
 * to understand the meaning of the context and should react when the context is changing
 * to reflect the changes accordingly.
 * <p>
 * For instance, let's assume we have state 'logged' with meanings
 * 1 - User is authorized, 2 - unknown user, 3 - operation in progress
 * so all components based on shared state should refreshe it's view and load child component based
 * on the fact what is the state.
 */
export interface ViewContextState {
    /** Limitation of service presented to the current user. Defines time from-to and by default only that actions are loaded to UI.  */
    period: TimePeriod,
    /** ID of currently edited Action, otherwide null */
    editedActionId: string | null
    // UI has adjust view to the fact of updating / creating some new Action
    lastTouchedActionId: EntityId | null

}

export const VIEWCONTEXT_PREV_PERIOD = "VIEWCONTEXT_PREV_PERIOD";
export const VIEWCONTEXT_NEXT_PERIOD = "VIEWCONTEXT_NEXT_PERIOD";
export const VIEWCONTEXT_PERIOD_SELECTED = "VIEWCONTEXT_PERIOD_SELECTED";

// Request to navigate to a separated view to edit an Action.
export const VIEWCONTEXT_ACTION_EDIT_START = "VIEWCONTEXT_ACTION_EDIT_START";
export const VIEWCONTEXT_ACTION_EDIT_CANCEL = "VIEWCONTEXT_ACTION_EDIT_CANCEL";
export const VIEWCONTEXT_ACTION_EDIT_UPDATED = "VIEWCONTEXT_ACTION_EDIT_UPDATED";

export interface PeriodPrev extends Action<typeof VIEWCONTEXT_PREV_PERIOD> {
    type: typeof VIEWCONTEXT_PREV_PERIOD,
    payload: {}
}

export interface PeriodNext extends Action<typeof VIEWCONTEXT_NEXT_PERIOD> {
    type: typeof VIEWCONTEXT_NEXT_PERIOD,
    payload: {}
}

export interface PeriodSelected extends Action<typeof VIEWCONTEXT_PERIOD_SELECTED> {
    type: typeof VIEWCONTEXT_PERIOD_SELECTED,
    payload: {
        requested: TimePeriod
    }
}

export interface ActionEditItem extends Action<typeof VIEWCONTEXT_ACTION_EDIT_START> {
    type: typeof VIEWCONTEXT_ACTION_EDIT_START,
    payload: {
        actionEntityId: string
    }
}

export interface ActionEditCancel extends Action<typeof VIEWCONTEXT_ACTION_EDIT_CANCEL> {
    type: typeof VIEWCONTEXT_ACTION_EDIT_CANCEL,
    payload: {}
}

export interface ActionEditUpdated extends Action<typeof VIEWCONTEXT_ACTION_EDIT_UPDATED> {
    type: typeof VIEWCONTEXT_ACTION_EDIT_UPDATED,
    payload: {
        lastTouchedEntityId: EntityId
    }
}

export type ViewContextAction = PeriodSelected
    | PeriodPrev
    | PeriodNext
    | ActionEditItem
    | ActionEditCancel
    | ActionEditUpdated;
export type ViewContextCommand = PeriodPrev | PeriodNext;
