import * as loginUserSagas from './loginUser';
import { Saga } from 'redux-saga';

const sagas = {
    ...loginUserSagas,
};

type sagaTypes = keyof typeof sagas;
export function registerWithMiddleware(middleware: { run: Function }) {
    for (var name in sagas) {
        const nameAsKey = name as sagaTypes;
        const saga = sagas[nameAsKey] as Saga;
        middleware.run(saga);
    }
}