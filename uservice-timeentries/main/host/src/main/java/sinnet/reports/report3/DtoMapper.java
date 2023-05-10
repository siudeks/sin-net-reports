package sinnet.reports.report3;

import io.vavr.collection.List;
import lombok.experimental.UtilityClass;

/** Converts ReportRequest (DTO) to its local model. */
@UtilityClass
class DtoMapper {

  static CustomerDetails fromDto(sinnet.report3.grpc.CustomerDetails x) {
    return new CustomerDetails(x.getName(), x.getAddress(), x.getCity());
  } 

  static GroupDetails fromDto(sinnet.report3.grpc.GroupDetails x) {
    return new GroupDetails(x.getPersonName(), List.ofAll(x.getDetailsList()).map(DtoMapper::fromDto));
  }
  
  static ReportRequest fromDto(sinnet.report3.grpc.ReportRequest dto) {
    return new ReportRequest(List.ofAll(dto.getDetailsList()).map(DtoMapper::fromDto));
  }
}
