import { SessionState, SessionAction, SignInFlow } from "./types";
import _ from "lodash";

const initialState: SessionState = {
    flow: SignInFlow.Unknown,
    idToken: "undefined",
    email: "invalid@email"
}

export const sessionReducer = (state = initialState, action: SessionAction): SessionState => {
    const clone = _.cloneDeep(state);
    switch (action.type) {
        case 'INITIATE_SESSION_STARTED':
            clone.flow = SignInFlow.SessionInitiated;
            return clone;
        case 'INITIATE_SESSION_FINISHED':
            clone.flow = SignInFlow.SessionEstablished
            clone.idToken = action.jwtToken;
            clone.email = action.email;
            return clone;
        default:
            return state;
    }
}
