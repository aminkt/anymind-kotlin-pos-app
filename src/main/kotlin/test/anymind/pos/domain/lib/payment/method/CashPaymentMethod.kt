package test.anymind.pos.domain.lib.payment.method

class CashPaymentMethod: APaymentMethod() {
    companion object Factory : PaymentMethodFactory() {
        override fun buildPaymentMethod() = CashPaymentMethod()
    }

    override fun getName(): String {
        return "CASH"
    }

    override fun getMinPriceModifier(): Float {
        return 0.9f
    }

    override fun getMaxPriceModifier(): Float {
        return 1f
    }

    override fun getPointModifier(): Float {
        return 0.05f
    }
}