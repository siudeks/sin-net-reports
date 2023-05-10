import { ViewContextAction, ViewContextCommand, VIEWCONTEXT_PREV_PERIOD } from "./types"
import { TimePeriod } from "./TimePeriod"

export const previousPeriodCommand = (): ViewContextCommand => {
    return {
        type: VIEWCONTEXT_PREV_PERIOD,
        payload: { }
    }
}

export const nextPeriodCommand = (): ViewContextCommand => {
    return {
        type: "VIEWCONTEXT_NEXT_PERIOD",
        payload: { }
    }
}

export const periodSelected = (requested: TimePeriod): ViewContextAction => {
    return {
        type: "VIEWCONTEXT_PERIOD_SELECTED",
        payload: {
            requested
        }
    }
}

