package test.anymind.pos.domain.lib.payment.method

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

internal class MastercardPaymentMethodTest {
    @Test
    fun testCalculation() {
        val mastercardPM = MastercardPaymentMethod.Factory.buildPaymentMethod()
        val price = 100.0
        val priceModifier = 0.95f

        assertEquals(95.0, mastercardPM.calculateFinalPrice(price, priceModifier))
        assertEquals(3.0, mastercardPM.calculateFinalPoints(price))
    }

    @Test
    fun testInvalidPriceModifier() {
        assertThrowsExactly(IllegalStateException::class.java) {
            MastercardPaymentMethod.Factory.buildPaymentMethod().calculateFinalPrice(100.0, 0.8f)
        }
    }
}