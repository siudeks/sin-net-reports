package sinnet.reports.report2;

import io.vavr.collection.List;
import lombok.experimental.UtilityClass;
import sinnet.reports.shared.Kilometers;
import sinnet.reports.shared.Mapper;
import sinnet.reports.shared.Minutes;

/** Converts ReportRequest (DTO) to its local model. */
@UtilityClass
class DtoMapper {

  static ActivityDetails fromDto(sinnet.report2.grpc.ActivityDetails x) {
    return new ActivityDetails(Mapper.fromDto(x.getYearMonth()), x.getPersonName(), Kilometers.of(x.getHowFarInKms()), Minutes.of(x.getHowLongInMins()));
  }

  static ReportRequest fromDto(sinnet.report2.grpc.ReportRequest dto) {
    return new ReportRequest(List.ofAll(dto.getDetailsList()).map(it -> fromDto(it)));
  }
}
