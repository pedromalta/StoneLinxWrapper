package dev.pedromalta.stonelinxwrapper.helpers

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.linx.dtefmobile.model.Info
import com.linx.dtefmobile.sdk.MenuItem
import com.linx.dtefmobile.sdk.TransactionResult

fun Info.toReadableMap(): ReadableMap = Arguments.createMap().apply {
    putString("sdkVersion", sdkVersion)
    putString("sdkExternalVersion", sdkExternalVersion)
    putBoolean("isPrintSupported", isPrintSupported)
    putString("hardwareManufacturer", hardwareManufacturer)
    putString("hardwareModel", hardwareModel)
    putBoolean("isSmartPOS", isSmartPOS)
    putString("buildType", buildType)
    putString("profile", profile)
    putString("identifier", identifier)
    putString("companyCode", companyCode)
    putString("storeCode", storeCode)
    putString("pointOfSale", pointOfSale)
}

fun TransactionResult.toReadableMap(): ReadableMap = Arguments.createMap().apply {
    putInt("resultCode", resultCode)
    putInt("transactionCode", transactionCode)
    putInt("transactionType", type.value)
    putString("transactionAmmount", transactionAmount)
    putString("transactionDiscount", transactionDiscount)
    putString("authorizationCode", authorizationCode)
    putString("nsu", nsu)
    putString("authorizerNsuAdicional", authorizerNsuAdicional)
    putString("brandCode", brandCode)
    putString("brand", brand)
    putString("responseCode", responseCode)
    putString("receipt", receipt)
    putString("abbreviatedReceipt", abbreviatedReceipt)
    putString("customerSalesReceipt", customerSalesReceipt)
    putString("storeSalesReceipt", storeSalesReceipt)
    putString("errorMessage", errorMessage)
    putString("cardValidity", cardValidity)
    putString("cardNumber", cardNumber)
    putString("especialData", especialData)
    putInt("installment", installment ?: 0)
    putString("acquirerCode", acquirerCode)
    putString("acquirerName", acquirerName)
    putString("financialType", financialType)
    putString("operationType", operationType)

}

fun MenuItem.toReadableMap(): ReadableMap = Arguments.createMap().apply {
    putDouble("code", code.toDouble())
    putString("display", display)
    putString("externalCode", externalCode)
    putString("raw", raw)
}

fun ReactApplicationContext.sendEvent(eventName: String, params: WritableMap?) {
    getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
        .emit(eventName, params)
}