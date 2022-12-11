package test.anymind.pos.domain.usecase

import test.anymind.pos.domain.entity.TransactionEntity
import test.anymind.pos.domain.lib.payment.method.APaymentMethod
import test.anymind.pos.domain.repository.ITransactionRepo
import java.time.LocalDateTime


class MakePaymentUC(
    private val transactionRepo: ITransactionRepo,
    private val price: Double,
    private val priceModifier: Float,
    private val paymentMethod: APaymentMethod
) {
    fun execute(): MakePaymentResult {
        println(LocalDateTime.now().second)

        val finalPrice: Double = paymentMethod.calculateFinalPrice(price, priceModifier)
        val points: Double = paymentMethod.calculateFinalPoints(price)

        val transactionEntity = TransactionEntity(
            null,
            finalPrice,
            points,
            LocalDateTime.now(),
            paymentMethod.getAdditionalDataAsJsonString()
        )

        println(transactionEntity.price)
        println(transactionEntity.point)
        transactionRepo.saveAndFlush(transactionEntity)

        println(LocalDateTime.now().second)
        throw RuntimeException("Thread handling issue.")
    }
}

data class MakePaymentResult(
    val finalPrice: Double,
    val points: Double
)