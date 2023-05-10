package sinnet.reports.report1;

import java.io.ByteArrayOutputStream;

import com.google.common.base.Objects;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.SneakyThrows;
import sinnet.reports.shared.Fonts;
import sinnet.reports.shared.Kilometers;
import sinnet.reports.shared.Minutes;
import sinnet.reports.shared.Utils;

class ReportResults {

  Font myFont = Fonts.base();
  Font myFontBold = Fonts.bold();

  @SneakyThrows
  ReportResult apply(ReportRequest request) {
    var customer = request.customer();
    var customerName = Option.of(customer.customerName()).getOrElse("(Brak wskazanego kontrahenta)");
    var customerCity = Option.of(customer.customerCity()).getOrElse("(miejscowość)");
    var customerAddress = Option.of(customer.address()).getOrElse("(adres)");

    try (var os = new ByteArrayOutputStream()) {
      
      var document = new Document();
      var pdfWriter = PdfWriter.getInstance(document, os);

      document.open();

      var headParam = new Paragraph();
      headParam.add(new Chunk(customerName, myFontBold));
      var header = ", " + customerCity + " ul. " + customerAddress;
      headParam.add(new Chunk(header, myFont));
      document.add(headParam);

      var newLineParagraph = new Paragraph("\n");
      document.add(newLineParagraph);

      var addLine = Function3.of((PdfPTable table, Boolean footerOrHeader, Seq<CellParams> v) -> {
        for (var it : v) {
          var font = !footerOrHeader ? myFont : myFontBold;
          var adjustedFont = Fonts.adjust(font, it.sizeAdjustment());
          var p = new Paragraph(it.text(), adjustedFont);
          
          var cell = new PdfPCell(p);
          if (footerOrHeader) {
            cell.setBorderWidth(0);
          }
          cell.setHorizontalAlignment(it.alignment().getId());
          cell.setColspan(it.width().getWidth());
          table.addCell(cell);
        }
        return (Void) null;
      });

      var noTime = Minutes.of(0);
      // var (timedActivities, noTimedActivities) = request.activities.partition(_.howLongInMins != noTime)
      var partitions = request.activities().partition(it -> !Objects.equal(it.howLongInMins(), noTime));
      var timedActivities = partitions._1;

      var table1 = newTable();
      asTable(addLine.apply(table1), timedActivities);
      document.add(table1);
      document.add(newLineParagraph);

      var table2 = newTable();
      var noTimedActivities = partitions._2;
      tableSpecial(addLine.apply(table2), noTimedActivities);
      document.add(table2);
      document.add(newLineParagraph);

      // We have to invoke close method so that content of the document is written
      // to os and can be obtained as the result of the whole operation
      document.close();

      var asBytes = os.toByteArray();
      return new ReportResult(request, asBytes);
    }

  }

  class TableColumns {
    static TableColumn Col1widthServiceman = new TableColumn(50);
    static TableColumn Col2widthDay = new TableColumn(25);
    static TableColumn Col3widthDescription = new TableColumn(120);
    static TableColumn Col4widthDuration = new TableColumn(15);
    static TableColumn Col5widthDistance = new TableColumn(15);
    static Integer width = Col1widthServiceman.getWidth()
        + Col2widthDay.getWidth()
        + Col3widthDescription.getWidth()
        + Col4widthDuration.getWidth()
        + Col5widthDistance.getWidth();
  }


  private PdfPTable newTable() {
    var table = new PdfPTable(TableColumns.width);
    var maxWidthPercentage = 100;
    table.setWidthPercentage(maxWidthPercentage);
    return table;
  }

  // Serviceman has sometimes too long name to fit to one line, so minimizing font fits to todays data
  Option<Integer> servicemanNameSizeAdjustment = Option.of(-4);

  private void asTable(Function2<Boolean, Seq<CellParams>, Void> addValue, Seq<ActivityDetails> activities) {

    addValue.apply(false, List.of(
      new CellParams("Serwisant", TableColumns.Col1widthServiceman, HorizontalAlignment.CENTER, Option.none()),
      new CellParams("Dzień", TableColumns.Col2widthDay, HorizontalAlignment.CENTER, Option.none()),
      new CellParams("Praca wykonana", TableColumns.Col3widthDescription, HorizontalAlignment.CENTER, Option.none()),
      new CellParams("Czas", TableColumns.Col4widthDuration, HorizontalAlignment.RIGHT, Option.none()),
      new CellParams("KM", TableColumns.Col5widthDistance, HorizontalAlignment.RIGHT, Option.none())));

    for (var item : activities) {
      var howLong = item.howLongInMins();
      var distance = item.howFarInKms();
      var who = item.who();
      addValue.apply(false, List.of(
        new CellParams(who, TableColumns.Col1widthServiceman, HorizontalAlignment.LEFT, servicemanNameSizeAdjustment),
        new CellParams(Utils.maybeDateAsString(item.when()), TableColumns.Col2widthDay, HorizontalAlignment.CENTER, Option.none()),
        new CellParams(item.description(), TableColumns.Col3widthDescription, HorizontalAlignment.CENTER, Option.none()),
        new CellParams(howLong.asString(), TableColumns.Col4widthDuration, HorizontalAlignment.RIGHT, Option.none()),
        new CellParams(distance.toString(), TableColumns.Col5widthDistance, HorizontalAlignment.RIGHT, Option.none())));
    }

    var initialAcc = Tuple.of(Kilometers.of(0), Minutes.of(0));
    // var (howFar, howLong) = activities.foldLeft(initialAcc)((acc, v) => (acc._1 + v.howFarInKms, acc._2 + v.howLongInMins))
    var howFarAndhowLong = activities.foldLeft(initialAcc, (acc, v) -> Tuple.of(acc._1().add(v.howFarInKms()), acc._2().add(v.howLongInMins())));
    var howFar = howFarAndhowLong._1();
    var howLong = howFarAndhowLong._2();

    addValue.apply(true, List.of(
      new CellParams(null, TableColumns.Col1widthServiceman, HorizontalAlignment.LEFT, Option.none()),
      new CellParams(null, TableColumns.Col2widthDay, HorizontalAlignment.LEFT, Option.none()),
      new CellParams("Suma", TableColumns.Col3widthDescription, HorizontalAlignment.RIGHT, Option.none()),
      new CellParams(howLong.asString(), TableColumns.Col4widthDuration, HorizontalAlignment.RIGHT, Option.none()),
      new CellParams(howFar.toString(), TableColumns.Col5widthDistance, HorizontalAlignment.RIGHT, Option.none())));
  }

  private void tableSpecial(Function2<Boolean, Seq<CellParams>, Void> addValue, Seq<ActivityDetails> activities) {

    var col1 = TableColumns.Col1widthServiceman;
    var col2 = TableColumns.Col2widthDay;
    var col3 = TableColumns.Col3widthDescription;
    var col4 = TableColumns.Col4widthDuration;
    var col5 = TableColumns.Col5widthDistance;
    var cols = col1.add(col2).add(col3).add(col4).add(col5);

    addValue.apply(true, List.of(
        new CellParams("USŁUGI DODATKOWE", cols, HorizontalAlignment.LEFT, Option.none())));

    addValue.apply(false, List.of(
        new CellParams("Serwisant", col1, HorizontalAlignment.CENTER, Option.none()),
        new CellParams("Dzień", col2, HorizontalAlignment.CENTER, Option.none()),
        new CellParams("Praca wykonana", col3, HorizontalAlignment.CENTER, Option.none()),
        new CellParams("Czas", col4, HorizontalAlignment.CENTER, Option.none()),
        new CellParams("KM", col5, HorizontalAlignment.RIGHT, Option.none())));

    for (var item : activities) {
      var howLong = item.howLongInMins();
      var distance = item.howFarInKms();
      var who = item.who();
      addValue.apply(false, List.of(
        new CellParams(who, col1, HorizontalAlignment.LEFT, servicemanNameSizeAdjustment),
        new CellParams(Utils.maybeDateAsString(item.when()), col2, HorizontalAlignment.CENTER, Option.none()),
        new CellParams(item.description(), col3, HorizontalAlignment.CENTER, Option.none()),
        new CellParams("0:00", col4, HorizontalAlignment.RIGHT, Option.none()),
        new CellParams(distance.toString(), col5, HorizontalAlignment.RIGHT, Option.none())));
    }

    var initialAcc = Kilometers.of(0);
    var howFar = activities.foldLeft(initialAcc, (acc, v) -> (acc.add(v.howFarInKms())));

    addValue.apply(true, List.of(
      new CellParams(null, col1, HorizontalAlignment.LEFT, Option.none()),
      new CellParams(null, col2, HorizontalAlignment.LEFT, Option.none()),
      new CellParams("Suma", col3, HorizontalAlignment.RIGHT, Option.none()),
      new CellParams("0:00", col4, HorizontalAlignment.RIGHT, Option.none()),
      new CellParams(howFar.toString(), col5, HorizontalAlignment.RIGHT, Option.none())));

  }
}
