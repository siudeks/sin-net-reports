import { EntityId } from "../store/actions/ServiceModel";
import { LocalDate } from "../store/viewcontext/TimePeriod";

export interface ServiceListModel extends EntityId {
  servicemanEmail: string | undefined,
  description: string | undefined,
  when: LocalDate,
  duration: number | undefined,
  distance: number | undefined
  customerName: string | undefined
}
