package test.anymind.pos.domain.usecase

import test.anymind.pos.domain.entity.TransactionEntity
import test.anymind.pos.domain.lib.payment.method.APaymentMethod
import test.anymind.pos.domain.repository.ITransactionRepo
import java.time.LocalDateTime

class MakePaymentUC(
    private val transactionRepo: ITransactionRepo,
    private val price: Double,
    private val priceModifier: Float,
    private val userId: Int,
    private val customerId: Int,
    private val paymentMethod: APaymentMethod
) {
    fun execute(): MakePaymentResult {
        println(LocalDateTime.now().second)

        val finalPrice: Double = paymentMethod.calculateFinalPrice(price, priceModifier)
        val points: Double = paymentMethod.calculateFinalPoints(price)

        val transactionEntity = TransactionEntity(
            null,
            userId,
            customerId,
            finalPrice,
            points,
            LocalDateTime.now(),
            paymentMethod.getAdditionalDataAsJsonString()
        )
        transactionRepo.saveAndFlush(transactionEntity)
        return MakePaymentResult(finalPrice, points)
    }
}

data class MakePaymentResult(
    val finalPrice: Double,
    val points: Double
)