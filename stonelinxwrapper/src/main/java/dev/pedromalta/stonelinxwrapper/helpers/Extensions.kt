package dev.pedromalta.stonelinxwrapper.helpers

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
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

fun ReactApplicationContext.sendEvent(eventName: String, params: WritableMap?) {
    getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
        .emit(eventName, params)
}