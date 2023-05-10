import { TimePeriod, LocalDate } from "../store/viewcontext/TimePeriod"

export const asDtoDate = (it : LocalDate) => {
    return ('0000' + it.year).substr(-4) + "-" +
           ('00' + it.month).substr(-2) + "-" +
           ('00' + it.day).substr(-2);
}

export const asDtoDates = (entry: TimePeriod) => {
    const current = entry.getValue();
    return {
        dateFrom: asDtoDate(current.dateFrom),
        dateTo: asDtoDate(current.dateTo)
    }
}

