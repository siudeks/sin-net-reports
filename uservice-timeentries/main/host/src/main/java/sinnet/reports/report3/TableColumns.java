package sinnet.reports.report3;

import lombok.experimental.UtilityClass;

@UtilityClass
class TableColumns {
  static TableColumn Col1widthDescription = new TableColumn(3);
  static TableColumn Col2widthNoted = new TableColumn(7);
  static int width = Col1widthDescription.width()
      + Col2widthNoted.width();
}
