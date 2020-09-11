import { LocalDate } from "../store/viewcontext/TimePeriod";

export const toModel = (dtoDate: string): LocalDate => {
    const yearAsString = dtoDate.substring(0,4);
    const monthAsString = dtoDate.substring(5, 7);
    const dayAsString = dtoDate.substring(8, 10);
    const year = Number(yearAsString);
    const month = Number(monthAsString);
    const day = Number(dayAsString);

    return { year, month, day };
}