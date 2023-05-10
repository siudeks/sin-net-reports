package sinnet

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions
import sinnet.report1.grpc.{
  ReportRequest => ReportRequestDTO,
  ActivityDetails => ActivityDetailsDTO,
  CustomerDetails => CustomerDetailsDTO}
import sinnet.reports.grpc.Date
import java.time.LocalDate
import sinnet.report1.Mapper
import sinnet.report1.ReportRequest
import sinnet.report1.CustomerDetails
import sinnet.report1.ActivityDetails
import sinnet.reports.Kilometers
import sinnet.reports.Minutes

class MapperTests {

  @Test
  def shouldConvertWhenEmptyActivityIsProvided(): Unit = {
    val minModel = Mapper(ReportRequestDTO.newBuilder().addDetails(ActivityDetailsDTO.newBuilder().build()).build())
    // no exceptions mean success
  }

  @Test
  def shouldConverSomeFullModel(): Unit = {
    val dto = ReportRequestDTO
      .newBuilder()
      .setCustomer(
        CustomerDetailsDTO.newBuilder()
          .setCustomerAddress("my customer address")
          .setCustomerCity("my customer city")
          .setCustomerId("my customer id")
          .setCustomerName("my customer name")
          .build())
      .addDetails(ActivityDetailsDTO.newBuilder()
          .setDescription("activity 1 description")
          .setHowFarInKms(11)
          .setHowLongInMins(12)
          .setWho("operator 1")
          .setWhen(Date.newBuilder().setYear(2001).setMonth(1).setDayOfTheMonth(12).build()))
      .addDetails(ActivityDetailsDTO.newBuilder()
          .setDescription("activity 2 description")
          .setHowFarInKms(21)
          .setHowLongInMins(22)
          .setWho("operator 2")
          .setWhen(Date.newBuilder().setYear(2002).setMonth(2).setDayOfTheMonth(22).build()))
      .build()

    val expected = ReportRequest(
      CustomerDetails("my customer name", "my customer city", "my customer address"),
      Seq(
        ActivityDetails("activity 1 description", "operator 1", Some(LocalDate.of(2001,1,12)), Minutes(12), Kilometers(11)),
        ActivityDetails("activity 2 description", "operator 2", Some(LocalDate.of(2002,2,22)), Minutes(22), Kilometers(21))
      )
    )
    val actual = Mapper(dto)
    Assertions.assertThat(actual).isEqualTo(expected)
  }
}
