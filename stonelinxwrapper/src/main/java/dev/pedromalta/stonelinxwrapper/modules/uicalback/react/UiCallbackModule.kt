package dev.pedromalta.stonelinxwrapper.modules.uicalback.react

import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import dev.pedromalta.stonelinxwrapper.helpers.GetFullModuleName
import dev.pedromalta.stonelinxwrapper.modules.uicalback.EventDrivenUiCallback

class UiCallbackModule(
    private val reactContext: ReactApplicationContext,
) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return GetFullModuleName("UiCallback")
    }

    @ReactMethod
    fun getCanceledStatus(
        uniqueID: String,
        onCallback: Callback,
    ) {
        val uiCallback = EventDrivenUiCallback.getUiCallback(uniqueID)
        if (uiCallback != null) {
            onCallback.invoke(null, uiCallback.status)
        } else {
            onCallback.invoke(true, "callback not found")

        }
    }

    @ReactMethod
    fun cancel(
        uniqueID: String,
    ) {
        EventDrivenUiCallback.getUiCallback(uniqueID)?.cancel()
    }

    @ReactMethod
    fun confirmResult(
        uniqueID: String,
        confirmed: Boolean,
    ) {
        EventDrivenUiCallback.getUiCallback(uniqueID)?.confirmResult(confirmed)
    }

    @ReactMethod
    fun returnLastStep(
        uniqueID: String,
        confirmed: Boolean,
    ) {
        EventDrivenUiCallback.getUiCallback(uniqueID)?.returnLastStep()
    }
}