package dev.pedromalta.stonelinxwrapper.helpers

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableMap
import com.linx.dtefmobile.model.Info

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