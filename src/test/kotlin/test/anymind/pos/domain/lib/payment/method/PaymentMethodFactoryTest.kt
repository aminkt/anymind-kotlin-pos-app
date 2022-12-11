package test.anymind.pos.domain.lib.payment.method

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PaymentMethodFactoryTest {
    @Test
    fun `test create payment method by name`() {
        PaymentMethodFactory.paymentMethodMapping.forEach { name, paymentMethodObject ->
            println("[!] Checking $name ...")
            val factoryObj = PaymentMethodFactory.buildByName(name)
            assertEquals(paymentMethodObject, factoryObj)
        }
    }
}