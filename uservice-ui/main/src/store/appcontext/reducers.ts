import { AppContextAction, AppState } from "./types";

const initialState: AppState = {
    empty: true,
    projectId: 'invalid UUID [1]'
}

export const appContextReducer = (state = initialState, action: AppContextAction): AppState => {
    if (action.type === "APPCONTEXT_READY") {
        return { empty: false, projectId: action.projectId };
    }
    return state;
}
