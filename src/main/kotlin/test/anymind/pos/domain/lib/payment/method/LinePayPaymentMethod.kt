package test.anymind.pos.domain.lib.payment.method

class LinePayPaymentMethod: APaymentMethod() {
    companion object Factory : PaymentMethodFactory() {
        override fun buildPaymentMethod() = LinePayPaymentMethod()
    }

    override fun getName(): String {
        return "LINE PAY"
    }

    override fun getMinPriceModifier(): Float {
        return 1f
    }

    override fun getMaxPriceModifier(): Float {
        return 1f
    }

    override fun getPointModifier(): Float {
        return 0.01f
    }
}