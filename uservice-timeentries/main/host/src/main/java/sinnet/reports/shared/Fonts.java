package sinnet.reports.shared;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;

import io.vavr.control.Option;
import lombok.experimental.UtilityClass;

/** TBD. */
@UtilityClass
public class Fonts {

  static {
    // without the initialization font discovery (method: FontFactory.getFont) does not find proper fonts
    // and all fonts in the report will stay with some default font
    FontFactory.registerDirectories();
  }

  /** TBD. */
  public static Font base() {
    // Fonts should be available because they are part of the project and they are included in proper
    // location in target docker image.
    var fontSize = 10;
    var baseFont = FontFactory.getFont("OpenSans");
    baseFont.setSize(fontSize);
    return baseFont;
  }

  /** TBD. */
  public static Font bold() {
    // Fonts should be available because they are part of the project and they are included in proper
    // location in target docker image.
    var fontSize = 10;
    var baseFont = FontFactory.getFont("OpenSans", 10, Font.BOLD);
    baseFont.setSize(fontSize);
    return baseFont;
  }

  /** TBD. */
  public static Font adjust(Font base, Option<Integer> sizeAdjustment) {
    return sizeAdjustment
      .map(it -> new Font(base.getBaseFont(), base.getSize() + it))
      .getOrElse(base);
  } 
}
