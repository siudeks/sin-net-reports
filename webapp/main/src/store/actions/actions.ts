import { ServiceAction, RELOAD_SERVICE_LIST, ADD_SERVICE_COMMAND as ADD_SERVICE_COMMAND, AddServiceCommand } from "./types"
import { ServiceAppModel } from "./ServiceModel"
import { TimePeriod } from "../viewcontext/TimePeriod"

export const removeService = (): ServiceAction => {
    return {
        type: "REMOVE_SERVICE",
        payload: {
        }
    }
}

export const reloadServicesBegin = (): ServiceAction => {
    return {
        type: RELOAD_SERVICE_LIST,
        payload: { }
    };
}



export const reloadServicesEnd = (items: ServiceAppModel[]): ServiceAction => {
    return {
        type: "REFRESH_SERVICE_LIST_END",
        payload: {
            items: items
        }
    };
}