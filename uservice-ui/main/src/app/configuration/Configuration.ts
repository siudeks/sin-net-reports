export enum WORK_MODE {
  /** Frontend and backend services are started locally. */
  DEV,
  /** Frontend and backend are started on prod env. */
  PROD
}

export const workMode = () => {
  if (process.env.NODE_ENV === 'production') {
    return WORK_MODE.PROD
  }

  return WORK_MODE.DEV;
}