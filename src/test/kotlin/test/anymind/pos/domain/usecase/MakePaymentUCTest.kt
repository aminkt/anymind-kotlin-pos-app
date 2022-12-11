package test.anymind.pos.domain.usecase

import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import test.anymind.pos.domain.repository.ITransactionRepo

@DataJpaTest
internal class MakePaymentUCTest {
    @Autowired
    lateinit var transactionRepo: ITransactionRepo

    @Test
    fun `test success story`() {
        println(transactionRepo.count())
    }
}