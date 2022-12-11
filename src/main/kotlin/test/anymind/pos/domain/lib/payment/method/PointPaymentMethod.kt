package test.anymind.pos.domain.lib.payment.method

class PointPaymentMethod: APaymentMethod() {
    override fun getName(): String {
        return "POINTS"
    }

    override fun getMinPriceModifier(): Float {
        return 1f
    }

    override fun getMaxPriceModifier(): Float {
        return 1f
    }

    override fun getPointModifier(): Float {
        return 0f
    }
}