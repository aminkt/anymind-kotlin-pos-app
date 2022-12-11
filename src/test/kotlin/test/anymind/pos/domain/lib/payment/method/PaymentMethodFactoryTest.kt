package test.anymind.pos.domain.lib.payment.method

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PaymentMethodFactoryTest {
    @Test
    fun createPaymentMethodByName() {
        PaymentMethodFactory.paymentMethodMapping.forEach { name, paymentMethodObject ->
            println("[!] Checking $name ...")
            val factoryObj = PaymentMethodFactory.buildByName(name)
            assertEquals(paymentMethodObject, factoryObj)
        }
    }
}