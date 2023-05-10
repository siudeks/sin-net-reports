package sinnet.reports.report2;

import lombok.experimental.UtilityClass;

@UtilityClass
class TableColumns {
  static TableColumn Col1widthPeriod = new TableColumn(2);
  static TableColumn Col2widthPerson = new TableColumn(10);
  static TableColumn Col3widthHours = new TableColumn(2);
  static TableColumn Col4widthKms = new TableColumn(2);
  static int width = Col1widthPeriod.width()
      + Col2widthPerson.width()
      + Col3widthHours.width()
      + Col4widthKms.width();
}
