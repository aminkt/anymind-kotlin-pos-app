package test.anymind.pos.domain.lib.payment.method

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MastercardPaymentMethod : APaymentMethod() {
    companion object Factory : PaymentMethodFactory() {
        override fun buildPaymentMethod() = MastercardPaymentMethod()
    }

    override fun getName(): String {
        return "MASTERCARD"
    }

    override fun getMinPriceModifier(): Float {
        return 0.95f
    }

    override fun getMaxPriceModifier(): Float {
        return 1f
    }

    override fun getPointModifier(): Float {
        return 0.03f
    }

    override fun addAdditionalData(additionalItemsJsonString: String?) {
        checkNotNull(additionalItemsJsonString) { "additionalItems is required!" }
        val additionalItemMap = Json.decodeFromString<Map<String, String>>(additionalItemsJsonString)
        check(additionalItemMap.containsKey("last4CardNumber")) { "You missed last4CardNumber in additionalItems." }
        additionalItems = Json.encodeToString(mapOf("courierService" to additionalItemMap["courierService"]))
    }
}