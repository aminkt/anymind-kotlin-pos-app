package test.anymind.pos.domain.usecase

import org.springframework.stereotype.Service
import test.anymind.pos.domain.entity.TransactionEntity
import test.anymind.pos.domain.lib.payment.method.APaymentMethod
import test.anymind.pos.domain.repository.ITransactionRepo
import java.time.LocalDateTime

@Service
class MakePaymentUC(private val transactionRepo: ITransactionRepo) {
    fun execute(inputDTO: MakePaymentInputDTO): MakePaymentResult {
        println(LocalDateTime.now().second)

        val finalPrice: Double = inputDTO.paymentMethod.calculateFinalPrice(inputDTO.price, inputDTO.priceModifier)
        val points: Double = inputDTO.paymentMethod.calculateFinalPoints(inputDTO.price)

        val transactionEntity = TransactionEntity(
            null,
            inputDTO.userId,
            inputDTO.customerId,
            finalPrice,
            points,
            LocalDateTime.now(),
            inputDTO.paymentMethod.getAdditionalDataAsJsonString()
        )
        transactionRepo.saveAndFlush(transactionEntity)
        return MakePaymentResult(finalPrice, points)
    }
}

data class MakePaymentInputDTO(
    val price: Double,
    val priceModifier: Float,
    val userId: Int,
    val customerId: Int,
    val paymentMethod: APaymentMethod
)
data class MakePaymentResult(
    val finalPrice: Double,
    val points: Double
)