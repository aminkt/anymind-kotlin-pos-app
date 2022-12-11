package test.anymind.pos.domain.lib.payment.method

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChequePaymentMethod: APaymentMethod() {
    companion object Factory : PaymentMethodFactory() {
        override fun buildPaymentMethod() = ChequePaymentMethod()
    }

    override fun getName(): String {
        return "CHEQUE"
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

    override fun addAdditionalData(additionalItemsJsonString: String?) {
        checkNotNull(additionalItemsJsonString) { "additionalItems is required!" }
        val additionalItemMap = Json.decodeFromString<Map<String, String>>(additionalItemsJsonString)
        check(additionalItemMap.containsKey("chequeNumber")) { "You missed chequeNumber in additionalItems." }
        additionalItems = Json.encodeToString(mapOf("chequeNumber" to additionalItemMap["chequeNumber"]))
    }
}