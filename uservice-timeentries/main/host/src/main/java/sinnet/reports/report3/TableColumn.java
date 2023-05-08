package sinnet.reports.report3;

// List of all columns available for activities.
// In some logic we can merge some of them (using e.g. sum of two values for one column)
// but finally we should use all columns so that all tables create using such columns will have
// similar location of columns.
record TableColumn(Integer width) {

  TableColumn add(TableColumn that) {
    return new TableColumn(this.width + that.width);
  }
}
