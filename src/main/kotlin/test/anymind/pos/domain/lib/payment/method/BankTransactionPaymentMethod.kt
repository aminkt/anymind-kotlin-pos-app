package test.anymind.pos.domain.lib.payment.method

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class BankTransactionPaymentMethod: APaymentMethod() {
    override fun getName(): String {
        return "BANK TRANSACTION"
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
        check(additionalItemMap.containsKey("accountNumber")) { "You missed accountNumber in additionalItems." }
        additionalItems = Json.encodeToString(mapOf("accountNumber" to additionalItemMap["accountNumber"]))
    }
}