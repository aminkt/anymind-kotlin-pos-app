package test.anymind.pos.app.view

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import test.anymind.pos.domain.repository.ITransactionRepo
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@GraphQLDescription("Sale report item.")
data class HourlySaleReportView(
    val dateTime: String,
    val sales: Double,
    val points: Double
) {
    companion object {
        fun getHourlySaleReport(transactionRepo: ITransactionRepo, userId: Int, startDateTime: String, endDateTime: String): List<HourlySaleReportView> {
            val report = transactionRepo.findHourlySaleReport(
                userId,
                OffsetDateTime.parse(startDateTime).toLocalDateTime(),
                OffsetDateTime.parse(endDateTime).toLocalDateTime()
            )

            val result = arrayListOf<HourlySaleReportView>()
            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"))
            report.forEach {
                result.add(
                    HourlySaleReportView(
                        dateTime = ZonedDateTime.parse(it.date_time, dateTimeFormatter).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                        sales = it.sales,
                        points = it.points
                    )
                )
            }

            return result
        }
    }
}
