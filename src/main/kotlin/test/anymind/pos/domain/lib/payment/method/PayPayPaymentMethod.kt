package test.anymind.pos.domain.lib.payment.method

class PayPayPaymentMethod: APaymentMethod() {
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