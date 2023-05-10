export type AppContextAction = AppContextReady | AppContextEmpty

export interface AppState {
    empty: boolean
    projectId: string        
}
interface AppContextEmpty {
    type: 'APPCONTEXT_EMPTY'
}

interface AppContextReady {
    type: 'APPCONTEXT_READY'
    projectId: string;   
}
