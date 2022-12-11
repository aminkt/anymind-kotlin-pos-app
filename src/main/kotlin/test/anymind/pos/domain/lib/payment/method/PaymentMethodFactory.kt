package test.anymind.pos.domain.lib.payment.method

abstract class PaymentMethodFactory {
    companion object {
        val paymentMethodMapping = mapOf(
            "CASH" to CashPaymentMethod(),
            "CASH_ON_DELIVERY" to CashOnDeliveryPaymentMethod(),
            "VISA" to VisaPaymentMethod(),
            "MASTERCARD" to MastercardPaymentMethod(),
            "AMEX" to AmexPaymentMethod(),
            "JCB" to JcbPaymentMethod(),
            "LINE PAY" to LinePayPaymentMethod(),
            "PAYPAY" to PayPayPaymentMethod(),
            "POINTS" to PointPaymentMethod(),
            "BANK TRANSACTION" to BankTransactionPaymentMethod(),
            "CHEQUE" to ChequePaymentMethod(),
        )

        fun buildByName(name: String): APaymentMethod
        {
            val paymentMethodObject = paymentMethodMapping[name]
            if (paymentMethodObject != null) {
                return paymentMethodObject
            }

            throw ArrayIndexOutOfBoundsException("$name is not a valid payment method name.")
        }
    }

    abstract fun buildPaymentMethod(): APaymentMethod
}