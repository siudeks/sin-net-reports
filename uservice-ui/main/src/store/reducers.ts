import { combineReducers } from "redux";
import { appContextReducer } from "./appcontext/reducers";
import { sessionReducer } from "./session/reducers";
import { viewContextReducer } from "./viewcontext/reducers";


export const rootReducer = combineReducers({
    auth: sessionReducer,
    viewContext: viewContextReducer,
    appState: appContextReducer
})
export type RootState = ReturnType<typeof rootReducer>;

