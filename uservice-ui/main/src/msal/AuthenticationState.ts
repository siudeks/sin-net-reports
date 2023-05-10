interface Initial {
    kind: 'INITIAL';
}

interface Authenticated {
    kind: 'AUTHENTICATED';
    logout: () => void;
}

interface Unauthenticated {
    kind: 'UNAUTHENTICATED';
    login: () => void;
}

export type AuthenticationState = Initial
    | Authenticated
    | Unauthenticated;
