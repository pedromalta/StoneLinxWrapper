package dev.pedromalta.stonelinxwrapper

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.JavaScriptModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.NativeModule
import com.facebook.react.uimanager.ViewManager
import dev.pedromalta.stonelinxwrapper.modules.auth.react.AuthenticationModule
import dev.pedromalta.stonelinxwrapper.modules.info.react.InfoModule

class LinxPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(
            AuthenticationModule(reactContext),
            InfoModule(reactContext),
        )
    }

    override fun createJSModules(): MutableList<Class<out JavaScriptModule>> {
        return mutableListOf()
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}
