package test.anymind.pos.domain.usecase

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import test.anymind.pos.domain.entity.TransactionEntity
import test.anymind.pos.domain.lib.payment.method.VisaPaymentMethod
import test.anymind.pos.domain.repository.ITransactionRepo
import java.util.*

@DataJpaTest
internal class MakePaymentUCTest {
    @Autowired
    lateinit var transactionRepo: ITransactionRepo

    @Test
    fun `test success story`() {
        val paymentUC = MakePaymentUC(transactionRepo)
        val res = paymentUC.execute(
            MakePaymentInputDTO(
                100.0,
                0.95f,
                100,
                200,
                VisaPaymentMethod.Factory.buildPaymentMethod()
            )
        )

        assertEquals(95.0, res.finalPrice)
        assertEquals(3.0, res.points)
        assertTrue(transactionRepo.count() > 0)

        val transactionEntity: Optional<TransactionEntity> = transactionRepo.findById(1)
        assertEquals(95.0, transactionEntity.get().price)
        assertEquals(3.0, transactionEntity.get().point)
        assertEquals(100, transactionEntity.get().userId)
        assertEquals(200, transactionEntity.get().customerId)
    }
}