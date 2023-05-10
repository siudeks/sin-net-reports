package sinnet.reports.report2;

record TableColumn(Integer width) {

  TableColumn add(TableColumn that) {
    return new TableColumn(this.width() + that.width());
  }
}
