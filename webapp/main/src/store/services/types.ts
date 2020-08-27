import { ServiceAppModel } from "./ServiceModel";
import { Action } from "redux";

export interface ServicesState {
    numerOfItems: number;
    items: ServiceAppModel[];    
}

export const ADD_SERVICE = "ADD_SERVICE";
export const REMOVE_SERVICE = "REMOVE_SERVICE";
export const REFRESH_SERVICE_LIST_END = "REFRESH_SERVICE_LIST_END";
export const REFRESH_SERVICE_LIST_BEGIN = "REFRESH_SERVICE_LIST_BEGIN";
export const CHANGE_VIEW_CONTEXT = "REFRESH_SERVICE_LIST_BEGIN";

export interface AddServiceAction extends Action<typeof ADD_SERVICE> {
    type: typeof ADD_SERVICE,
    payload: {
        description: string
    }
}

interface RemoveServiceAction {
    type: typeof REMOVE_SERVICE
    payload: {

    }
}

interface RefreshServiceAction {
    type: typeof REFRESH_SERVICE_LIST_END,
    payload: {
        items: ServiceAppModel[]
    }
}

export interface RefreshServiceRequestAction {
    type: typeof REFRESH_SERVICE_LIST_BEGIN,
    payload: {
        year: number,
        month: number
    }
}

export type ServiceActionType = typeof ADD_SERVICE | typeof REMOVE_SERVICE | typeof REFRESH_SERVICE_LIST_END | typeof REFRESH_SERVICE_LIST_BEGIN;
export type ServiceAction = AddServiceAction | RemoveServiceAction | RefreshServiceAction | RefreshServiceRequestAction;
