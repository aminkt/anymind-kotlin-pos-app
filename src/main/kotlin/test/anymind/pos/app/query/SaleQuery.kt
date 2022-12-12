package test.anymind.pos.app.query

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import test.anymind.pos.app.view.HourlySaleReportView
import test.anymind.pos.app.view.PaymentMethodView
import test.anymind.pos.domain.repository.ITransactionRepo

@Component
class SaleQuery(val transactionRepo: ITransactionRepo) : Query {
    fun paymentMethod(name: String): PaymentMethodView {
        return PaymentMethodView.getPaymentMethodByName(name)
    }

    suspend fun hourlySaleReport(input: SaleReportInput, dfe: DataFetchingEnvironment): List<HourlySaleReportView> {
        val userId = dfe.graphQlContext.get<Int>("userId")
        checkNotNull("userId") { "You have to login first." }
        return HourlySaleReportView.getHourlySaleReport(transactionRepo, userId, input.startDateTime, input.endDateTime)
    }
}

@GraphQLDescription("Send date time for start date and end date in ISO format.")
data class SaleReportInput(
    val startDateTime: String,
    val endDateTime: String
)