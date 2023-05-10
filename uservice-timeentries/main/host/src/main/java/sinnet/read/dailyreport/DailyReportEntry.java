package sinnet.read.dailyreport;

import java.time.LocalDate;

import jakarta.persistence.Table;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TBD.
 */
@NoArgsConstructor
@Data
@Table(name =  Const.PROJECTION_NAME)
public class DailyReportEntry {

  /** Unique id of th record. */
  @Id
  private Long id;

  /** When the service has been provided. */
  private LocalDate when;

  private String description;
}
