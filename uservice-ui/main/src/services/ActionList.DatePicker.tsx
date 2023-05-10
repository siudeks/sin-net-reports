import { Calendar, DayOfWeek, DateRangeType } from '@fluentui/react/lib/Calendar';
import { LocalDate } from '../store/viewcontext/TimePeriod';
import React from 'react';

export interface AppDatePickerProps {
  onSelectDate(value: LocalDate): void;
  /** Text displayed on the link used to change date to current date. */
  gotoTodayText: string;
  current: LocalDate;
}

const dayPickerStrings = {
  months: [
    'Styczeń',
    'Luty',
    'Marzec',
    'Kwiecień',
    'Maj',
    'Czerwiec',
    'Lipiec',
    'Sierpień',
    'Wrzesień',
    'Październik',
    'Listopad',
    'Grudzień',
  ],
  shortMonths: ['Sty', 'Lut', 'Mar', 'Kwie', 'Maj', 'Czer', 'Lip', 'Sier', 'Wrze', 'Paź', 'List', 'Gru'],
  days: ['Niedziela', 'Poniedziałek', 'Wtorek', 'Środa', 'Czwartek', 'Piątek', 'Sobota'],
  shortDays: ['N', 'P', 'W', 'Ś', 'C', 'P', 'S'],
  weekNumberFormatString: 'Numer tygodnia {0}',
  prevMonthAriaLabel: 'Poprzedni miesiąc',
  nextMonthAriaLabel: 'Następny miesiąc',
  prevYearAriaLabel: 'Poprzedni rok',
  nextYearAriaLabel: 'Następny rok',
  prevYearRangeAriaLabel: 'Previous year range',
  nextYearRangeAriaLabel: 'Next year range',
  closeButtonAriaLabel: 'Zamknij',
  monthPickerHeaderAriaLabel: '{0}, wybierz zmianę roku',
  yearPickerHeaderAriaLabel: '{0}, wybierz zmianę miesiąca',
};
const divStyle: React.CSSProperties = {
  height: 'auto',
};

export const AppDatePicker: React.FC<AppDatePickerProps> = props => {

  const { current: currentDate } = props;

  const onSelectDate = (date: Date, dateRangeArray?: Date[]): void => {
    const asLocalDate = LocalDate.of(date);
    props.onSelectDate(asLocalDate);
  };

  return (
    <div style={divStyle}>
      <Calendar
        // eslint-disable-next-line react/jsx-no-bind
        onSelectDate={onSelectDate}
        isDayPickerVisible={true}
        isMonthPickerVisible={true}
        dateRangeType={DateRangeType.Day}
        value={LocalDate.toDate(currentDate)!}
        firstDayOfWeek={DayOfWeek.Monday}
        strings={{ ...dayPickerStrings, goToToday: props.gotoTodayText }}
        highlightCurrentMonth={false}
        highlightSelectedMonth={true}
      />
    </div>
  );
};