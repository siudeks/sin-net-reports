package sinnet.reports.report2;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import sinnet.reports.shared.Fonts;
import sinnet.reports.shared.Kilometers;
import sinnet.reports.shared.Minutes;

@UtilityClass
class ReportResults {

  static Font myFont = Fonts.base();

  static Font myFontBold = Fonts.bold();

  static Void addLine(PdfPTable table, Boolean isFooter, Seq<CellParams> v) {
    for (var it : v) {
      var font = !isFooter ? myFont : myFontBold;

      var p = new Paragraph(it.text(), font);
      var cell = new PdfPCell(p);
      if (isFooter) {
        cell.setBorderWidth(0);
      }
      cell.setHorizontalAlignment(it.alignment().getId());
      cell.setColspan(it.width().width());
      table.addCell(cell);
    }
    return null;
  }

  @SneakyThrows
  static ReportResult apply(ReportRequest request) {
    try (var os = new ByteArrayOutputStream()) {
      var document = new Document();
      var pdfWriter = PdfWriter.getInstance(document, os);
      document.open();

      var table1 = newTable();
      var addLineFunction = Function3.of(ReportResults::addLine);
      asTable(addLineFunction.apply(table1), request.activities());
      document.add(table1);

      // We have to invoke close method so that content of the document is written
      // to os and can be obtained as the result of the whole operation
      document.close();

      var content = os.toByteArray();
      return new ReportResult(request, content);
    }

  }

  private PdfPTable newTable() {
    var table = new PdfPTable(TableColumns.width);
    var maxWidthPercentage = 100;
    table.setWidthPercentage(maxWidthPercentage);
    return table;
  }

  private void asTable(Function2<Boolean, Seq<CellParams>, Void> addValue, Seq<ActivityDetails> activities) {

    addValue.apply(false, List.of(
        new CellParams("Kiedy", TableColumns.Col1widthPeriod, HorizontalAlignment.CENTER),
        new CellParams("Opis", TableColumns.Col2widthPerson, HorizontalAlignment.CENTER),
        new CellParams("Czas", TableColumns.Col3widthHours, HorizontalAlignment.CENTER),
        new CellParams("Km", TableColumns.Col4widthKms, HorizontalAlignment.CENTER)));

    for (var item : activities) {
      var period = item.period();
      var description = item.personName();
      var minutes = item.minutes();
      var kilometers = item.kilometers();
      addValue.apply(false, List.of(
          new CellParams(period.toString(), TableColumns.Col1widthPeriod, HorizontalAlignment.LEFT),
          new CellParams(description.toString(), TableColumns.Col2widthPerson, HorizontalAlignment.LEFT),
          new CellParams(minutes.asString(), TableColumns.Col3widthHours, HorizontalAlignment.RIGHT),
          new CellParams(kilometers.toString(), TableColumns.Col4widthKms, HorizontalAlignment.RIGHT)));
    }

    var initialAcc = Tuple.of(Kilometers.of(0), Minutes.of(0));
    // var (howFar, howLong) = activities.foldLeft(initialAcc)((acc, v) => (acc._1 +
    // v.howFarInKms, acc._2 + v.howLongInMins))
    var howFarAndhowLong = activities.foldLeft(initialAcc,
        (acc, v) -> Tuple.of(acc._1.add(v.kilometers()), acc._2().add(v.minutes())));
    var howFar = howFarAndhowLong._1();
    var howLong = howFarAndhowLong._2();

    addValue.apply(true, List.of(
        new CellParams("", TableColumns.Col1widthPeriod, HorizontalAlignment.LEFT),
        new CellParams("Suma", TableColumns.Col2widthPerson, HorizontalAlignment.LEFT),
        new CellParams(howLong.asString(), TableColumns.Col3widthHours, HorizontalAlignment.RIGHT),
        new CellParams(howFar.toString(), TableColumns.Col4widthKms, HorizontalAlignment.RIGHT)));

  }
}
