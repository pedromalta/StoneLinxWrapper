package dev.pedromalta.stonelinxwrapper

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.JavaScriptModule
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import dev.pedromalta.stonelinxwrapper.modules.auth.react.AuthenticationModule
import dev.pedromalta.stonelinxwrapper.modules.transaction.react.TransactionModule
import dev.pedromalta.stonelinxwrapper.modules.info.react.InfoModule
import com.linx.mobile.logger.Log as LinxMobileLog

class LinxPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        // Manually initiate Linx Logger to avoid crashes on React Native
        LinxMobileLog.init(null)

        return listOf(
            AuthenticationModule(reactContext),
            TransactionModule(reactContext),
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
