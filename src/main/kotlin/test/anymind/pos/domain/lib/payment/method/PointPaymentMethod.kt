package test.anymind.pos.domain.lib.payment.method

class PointPaymentMethod: APaymentMethod() {
    companion object Factory : PaymentMethodFactory() {
        override fun buildPaymentMethod() = PointPaymentMethod()
    }

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