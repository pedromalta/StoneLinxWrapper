package dev.pedromalta.stonelinxwrapper.modules.info.react


import android.content.Context
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import dev.pedromalta.stonelinxwrapper.helpers.GetFullModuleName
import dev.pedromalta.stonelinxwrapper.helpers.toReadableMap
import dev.pedromalta.stonelinxwrapper.modules.info.Info

class InfoModule(
    private val reactContext: ReactApplicationContext,
) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return GetFullModuleName("Info")
    }

    @ReactMethod
    fun info(callback: Callback) {
        val context: Context = reactContext.applicationContext
        val info = Info().getInfo(context)

        callback.invoke(info.toReadableMap())

    }
}

