package sinnet.reports.report3;

import java.io.ByteArrayOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import sinnet.reports.shared.Fonts;

@UtilityClass
class ReportResults {

  static Font myFont = Fonts.base();
  static Font myFontBold = Fonts.bold();

  Void addLine(PdfPTable table, Boolean isFooter, Seq<CellParams> v) {
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
      var addLinePartial = Function3.of(ReportResults::addLine);
      asTable(addLinePartial.apply(table1), request.activities());
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

  private void asTable(Function2<Boolean, Seq<CellParams>, Void> addValue, Seq<GroupDetails> activities) {

    for (var item : activities) {
      var personName = item.personName();
      var clients = item.details();

      addValue.apply(true, List.of(
          new CellParams(personName, TableColumns.Col1widthDescription, HorizontalAlignment.LEFT),
          new CellParams("", TableColumns.Col2widthNoted, HorizontalAlignment.CENTER)));

      addValue.apply(false, List.of(
          new CellParams("Opis", TableColumns.Col1widthDescription, HorizontalAlignment.CENTER),
          new CellParams("Notatki", TableColumns.Col2widthNoted, HorizontalAlignment.CENTER)));

      for (var client : clients) {
        var name = client.name();
        var address = client.address();
        var city = client.city();

        var description = name + "\n" + address + "\n" + city;
        addValue.apply(false, List.of(
            new CellParams(description, TableColumns.Col1widthDescription, HorizontalAlignment.LEFT),
            new CellParams("", TableColumns.Col2widthNoted, HorizontalAlignment.LEFT)));
      }
    }

  }
}
