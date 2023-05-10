import React from "react";
import { LocalDate } from "../store/viewcontext/TimePeriod";

export const LocalDateView: React.FC<{item: LocalDate}> = props => {
    const separator = '/';
    const { item } = props;
    return (<> { ('00' + item.day).substr(-2) + separator + ('00' + item.month).substr(-2) + separator + ('0000' + item.year).substr(-4)}</>)
}
