package sinnet

import sinnet.reports.{ReportRequest => ReportRequestDTO, ReportRequests => ReportRequestsDTO, Response}
import java.time.LocalDate
import scala.collection.JavaConverters._
import scala.util.Try

/** Converts ReportRequest (DTO) to local model. */
object Mapper {
  private implicit def toLocalDate(x: sinnet.reports.Date): Option[LocalDate] = 
    Try(LocalDate.of(x.getYear(), x.getMonth(), x.getDayOfTheMonth())).toOption
  private implicit def toActivityDetails(x: sinnet.reports.ActivityDetails) =
    ActivityDetails(x.getDescription(), x.getWho(), x.getWhen(), new Minutes(x.getHowLongInMins()), new Kilometers(x.getHowFarInKms()))
  private implicit def toCustomerDetails(x: sinnet.reports.CustomerDetails) =
    CustomerDetails(x.getCustomerName(), x.getCustomerCity(), x.getCustomerAddress())

    
  implicit def apply(dto: ReportRequestDTO): ReportRequest = {
    ReportRequest(dto.getCustomer(), dto.getDetailsList().asScala.map(toActivityDetails _))
  }
}
