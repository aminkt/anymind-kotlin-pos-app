package test.anymind.pos.app.view

import test.anymind.pos.domain.lib.payment.method.PaymentMethodFactory

data class PaymentMethodView(
    val name: String,
    val minPriceModifier: Float,
    val maxPriceModifier: Float,
    val pointModifier: Float
) {
    companion object {
        fun getPaymentMethodByName(name: String): PaymentMethodView {
            val paymentMethodEntity = PaymentMethodFactory.buildByName(name)
            return PaymentMethodView(
                paymentMethodEntity.getName(),
                paymentMethodEntity.getMinPriceModifier(),
                paymentMethodEntity.getMaxPriceModifier(),
                paymentMethodEntity.getPointModifier()
            )
        }
    }
}