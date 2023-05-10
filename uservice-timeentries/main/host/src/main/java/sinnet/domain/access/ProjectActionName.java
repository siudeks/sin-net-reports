package sinnet.domain.access;

/** All known low-level actions (read and modify) supported by the uservice. */
enum ProjectActionName {

  /** Creation of new Project. */
  PROJECT_CREATE_NEW,
  /** Delete the whole project and also all entities owned by the project. */
  DELETE_PROJECT,
  /** Modyfy state of the project. */
  PROJECT_UPDATE,
  /** Add new operator to the project. */
  PROJECT_ASSIGN_OPERATORS,
  /** Get all user assignments for a particular project. */
  PROJECT_GET_ASSIGNMENTS,

}
