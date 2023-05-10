package sinnet.reports.report2;

import com.lowagie.text.alignment.HorizontalAlignment;

// List of all columns available for activities.
// In some logic we can merge some of them (using e.g. sum of two values for one
// column)
// but finally we should use all columns so that all tables create using such
// columns will have
// similar location of columns.
record CellParams(String text, TableColumn width, HorizontalAlignment alignment) {
}
