package sinnet.reports.report1;

import io.vavr.collection.List;
import io.vavr.control.Option;
import sinnet.reports.shared.Kilometers;
import sinnet.reports.shared.Minutes;

/** Converts ReportRequest (DTO) to its local model. */
class Mapper {

  private static ActivityDetails fromDto(sinnet.report1.grpc.ActivityDetails x) {
    var maybeWhen = Option.of(x.getWhen()).filter(it -> x.hasWhen()).flatMap(it -> sinnet.reports.shared.Mapper.fromDto(it));
    return new ActivityDetails(x.getDescription(), x.getWho(), maybeWhen, Minutes.of(x.getHowLongInMins()),
        Kilometers.of(x.getHowFarInKms()));
  }

  private static CustomerDetails fromDto(sinnet.report1.grpc.CustomerDetails x) {
    return new CustomerDetails(x.getCustomerName(), x.getCustomerCity(), x.getCustomerAddress());
  }

  public static ReportRequest fromDto(sinnet.report1.grpc.ReportRequest dto) {
    var items = List.ofAll(dto.getDetailsList()).map(Mapper::fromDto);
    var customer = fromDto(dto.getCustomer());
    return new ReportRequest(customer, items);
  }

}
