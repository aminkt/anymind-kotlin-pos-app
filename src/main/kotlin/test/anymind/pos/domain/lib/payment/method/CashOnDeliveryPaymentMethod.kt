package test.anymind.pos.domain.lib.payment.method

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CashOnDeliveryPaymentMethod : APaymentMethod() {
    companion object Factory : PaymentMethodFactory() {
        override fun buildPaymentMethod() = CashOnDeliveryPaymentMethod()
    }

    override fun getName(): String {
        return "CASH_ON_DELIVERY"
    }

    override fun getMinPriceModifier(): Float {
        return 1f
    }

    override fun getMaxPriceModifier(): Float {
        return 1.02f
    }

    override fun getPointModifier(): Float {
        return 0.05f
    }

    override fun addAdditionalData(additionalItemsJsonString: String?) {
        checkNotNull(additionalItemsJsonString) { "additionalItems is required!" }
        val additionalItemMap = Json.decodeFromString<Map<String, String>>(additionalItemsJsonString)
        check(additionalItemMap.containsKey("courierService")) { "courierService is required in additionalItems!" }

        val courierService = additionalItemMap["courierService"]
        val validCourierServiceList = listOf<String>(
            "YAMATO",
            "SAGAWA"
        )
        check(courierService in validCourierServiceList) { "$courierService is not valid. Valid items are $validCourierServiceList" }
        additionalItems = Json.encodeToString(mapOf("courierService" to additionalItemMap["courierService"]))
    }
}