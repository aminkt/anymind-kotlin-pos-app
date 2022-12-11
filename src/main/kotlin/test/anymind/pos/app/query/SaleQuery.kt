package test.anymind.pos.app.query

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import org.springframework.stereotype.Component
import test.anymind.pos.domain.lib.payment.method.PaymentMethodFactory
import test.anymind.pos.domain.repository.ITransactionRepo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class SaleQuery(): Query {
    fun paymentMethod(name: String): PaymentMethodView {
        val paymentMethodEntity = PaymentMethodFactory.buildByName(name)
        return PaymentMethodView(
            paymentMethodEntity.getName(),
            paymentMethodEntity.getMinPriceModifier(),
            paymentMethodEntity.getMaxPriceModifier(),
            paymentMethodEntity.getPointModifier()
        )
    }

    fun report(input: SaleReportInput): List<SaleReportView> {
        return mutableListOf<SaleReportView>(
            SaleReportView(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE), 100.0, 5.0),
            SaleReportView(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE), 120.0, 7.0),
            SaleReportView(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE), 130.0, 4.0),
        )
    }
}

data class PaymentMethodView(
    val name: String,
    val minPriceModifier: Float,
    val maxPriceModifier: Float,
    val pointModifier: Float
)

@GraphQLDescription("Sale report item.")
data class SaleReportView(
    val dateTime: String,
    val sale: Double,
    val point: Double
)

@GraphQLDescription("Send date time for start date and end date in ISO format.")
data class SaleReportInput(
    val startDateTime: String,
    val endDateTime: String
)