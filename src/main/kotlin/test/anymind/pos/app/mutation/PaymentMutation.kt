package test.anymind.pos.app.mutation

import com.expediagroup.graphql.server.operations.Mutation
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import test.anymind.pos.domain.lib.payment.method.PaymentMethodFactory
import test.anymind.pos.domain.repository.ITransactionRepo
import test.anymind.pos.domain.usecase.MakePaymentUC

@Component
class PaymentMutation(val transactionRepo: ITransactionRepo): Mutation {
    fun makePayment(paymentRequest: PaymentRequestInput): PaymentResultView {
        val paymentMethod = PaymentMethodFactory.buildByName(paymentRequest.paymentMethod)
        paymentMethod.addAdditionalData(paymentRequest.additionalItems)
        runBlocking {
            val res = MakePaymentUC(
                transactionRepo,
                paymentRequest.price,
                paymentRequest.priceModifier.toFloat(),
                paymentMethod
            ).execute()
            return@runBlocking PaymentResultView(
                res.finalPrice,
                res.points
            )
        }
        throw RuntimeException("Thread handling issue.")
    }
}

data class PaymentRequestInput(
    val customerId: String,
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