import { SessionAction, INITIATE_SESSION_STARTED } from "./types"

export const initiateSession = (): SessionAction => {
    return {
        type: INITIATE_SESSION_STARTED
    }
}

