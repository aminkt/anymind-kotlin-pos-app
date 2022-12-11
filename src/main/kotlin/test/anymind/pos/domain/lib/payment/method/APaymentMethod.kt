package test.anymind.pos.domain.lib.payment.method

import kotlin.math.roundToInt

abstract class APaymentMethod {
    protected var additionalItems: String? = null

    abstract fun getName(): String
    abstract fun getMinPriceModifier(): Float
    abstract fun getMaxPriceModifier(): Float
    abstract fun getPointModifier(): Float

    open fun addAdditionalData(additionalItemsJsonString: String?) {
        // Explicit data type checking is required. Please overwrite this method
        // in case additional items information is required.
    }

    open fun getAdditionalDataAsJsonString(): String? {
        return additionalItems
    }

    fun calculateFinalPrice(price: Double, priceModifier: Float): Double
    {
        check(priceModifier >= getMinPriceModifier()) { "priceModifier ($priceModifier) must be grater or equal than ${getMinPriceModifier()}!" }
        check(priceModifier <= getMaxPriceModifier()) { "priceModifier ($priceModifier) must be less or equal than ${getMaxPriceModifier()}!" }
        val finalPrice = price * priceModifier
        return round(finalPrice)
    }

    fun calculateFinalPoints(price: Double): Double
    {
        val finalPoint = price * getPointModifier()
        return round(finalPoint)
    }

    private fun round(num: Double): Double
    {
        return (num * 100.0).roundToInt() / 100.0
    }
}