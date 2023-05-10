import { LocalDate } from "../viewcontext/TimePeriod";

export interface ServiceAppModel extends EntityId {
  servicemanEmail: string | undefined,
  servicemanName: string | undefined,
  description: string | undefined,
  when: LocalDate,
  duration: number | undefined,
  distance: number | undefined
  customer: {
    id: EntityId,
    name: string
  } | undefined
}

export interface EntityId {
  projectId: string,
  entityId: string,
  entityVersion: number,
}