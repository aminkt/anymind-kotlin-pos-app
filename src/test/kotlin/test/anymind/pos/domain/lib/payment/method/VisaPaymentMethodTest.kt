package test.anymind.pos.domain.lib.payment.method

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

internal class VisaPaymentMethodTest {
    @Test
    fun testCalculation() {
        val visaPM = VisaPaymentMethod.Factory.buildPaymentMethod()
        val price = 100.0
        val priceModifier = 0.95f

        assertEquals(95.0, visaPM.calculateFinalPrice(price, priceModifier))
        assertEquals(3.0, visaPM.calculateFinalPoints(price))
    }

    @Test
    fun testInvalidPriceModifier() {
        assertThrowsExactly(IllegalStateException::class.java) {
            VisaPaymentMethod.Factory.buildPaymentMethod().calculateFinalPrice(100.0, 0.8f)
        }
    }
}