package test.anymind.pos.app.mutation

import com.expediagroup.graphql.server.operations.Mutation
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import test.anymind.pos.domain.lib.payment.method.PaymentMethodFactory
import test.anymind.pos.domain.repository.ITransactionRepo
import test.anymind.pos.domain.usecase.MakePaymentUC

@Component
class PaymentMutation(val transactionRepo: ITransactionRepo): Mutation {
    fun makePayment(paymentRequest: PaymentRequestInput, dfe: DataFetchingEnvironment): PaymentResultView {
        val userId = dfe.graphQlContext.get<Int>("userId")
        checkNotNull("userId") { "You have to login first." }

        val paymentMethod = PaymentMethodFactory.buildByName(paymentRequest.paymentMethod)
        paymentMethod.addAdditionalData(paymentRequest.additionalItems)
        val res = MakePaymentUC(
            transactionRepo,
            paymentRequest.price,
            paymentRequest.priceModifier.toFloat(),
            userId,
            paymentRequest.customerId,
            paymentMethod
        ).execute()

        return PaymentResultView(
            res.finalPrice,
            res.points
        )
    }
}

data class PaymentRequestInput(
    val customerId: Int,
    val dateTime: String,
    val paymentMethod: String,
    val price: Double,
    val priceModifier: Double,
    val additionalItems: String?
)

data class PaymentResultView(
    val finalPrice: Double,
    val points: Double
)