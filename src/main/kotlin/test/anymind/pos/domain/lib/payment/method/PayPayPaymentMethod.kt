package test.anymind.pos.domain.lib.payment.method

class PayPayPaymentMethod: APaymentMethod() {
    companion object Factory : PaymentMethodFactory() {
        override fun buildPaymentMethod() = PayPayPaymentMethod()
    }

    override fun getName(): String {
        return "PAYPAY"
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