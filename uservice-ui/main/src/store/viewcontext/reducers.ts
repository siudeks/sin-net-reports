import _ from "lodash";
import { ViewContextState, ViewContextAction, VIEWCONTEXT_ACTION_EDIT_START, VIEWCONTEXT_ACTION_EDIT_CANCEL, VIEWCONTEXT_ACTION_EDIT_UPDATED } from "./types";
import { TimePeriod } from "./TimePeriod";

const initialState: ViewContextState = {
    period: new TimePeriod(new Date()),
    // UI has to adjust View to the fact of being (or not) edited an Action
    editedActionId: null,
    lastTouchedActionId: null
}

export const viewContextReducer = (state = initialState, action: ViewContextAction): ViewContextState => {
    const clone = _.cloneDeep(state);
    switch (action.type) {
        case 'VIEWCONTEXT_PERIOD_SELECTED':
            var period = action.payload.requested;
            clone.period = period;
            return clone;
        case 'VIEWCONTEXT_PREV_PERIOD':
            clone.period = clone.period.prev();
            return clone;
        case 'VIEWCONTEXT_NEXT_PERIOD':
            clone.period = clone.period.next();
            return clone;
        case VIEWCONTEXT_ACTION_EDIT_START:
            clone.editedActionId = action.payload.actionEntityId;
            return clone;
        case VIEWCONTEXT_ACTION_EDIT_CANCEL:
            clone.editedActionId = null;
            return clone;
        case VIEWCONTEXT_ACTION_EDIT_UPDATED:
            clone.lastTouchedActionId = action.payload.lastTouchedEntityId;
            return clone;
        default:
            return state;
    }
}
