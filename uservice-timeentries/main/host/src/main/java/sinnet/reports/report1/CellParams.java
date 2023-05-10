package sinnet.reports.report1;

import com.lowagie.text.alignment.HorizontalAlignment;

import io.vavr.control.Option;

record CellParams(String text, TableColumn width, HorizontalAlignment alignment, Option<Integer> sizeAdjustment) { }
