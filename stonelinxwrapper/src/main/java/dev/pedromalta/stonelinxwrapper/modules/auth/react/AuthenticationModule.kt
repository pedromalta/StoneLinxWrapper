package dev.pedromalta.stonelinxwrapper.modules.auth.react


import android.content.Context
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import dev.pedromalta.stonelinxwrapper.modules.auth.Authentication
import dev.pedromalta.stonelinxwrapper.modules.auth.model.CredentialInfo
import dev.pedromalta.stonelinxwrapper.helpers.GetFullModuleName
import dev.pedromalta.stonelinxwrapper.view.NoOpUiCallback

class AuthenticationModule(
    private val reactContext: ReactApplicationContext,
) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return GetFullModuleName("Auth")
    }

    @ReactMethod
    fun authenticate(
        cnpj: String,
        accessToken: String,
        onCallback: Callback,
    ) {
        val context: Context = reactContext.applicationContext
        val auth = Authentication(context, NoOpUiCallback, CredentialInfo(cnpj, accessToken))

        auth.authenticate(
            onFailure = {
                onCallback.invoke(true)
            },
            onSuccess = { nsu ->
                onCallback.invoke(false, nsu)
            },
        )
    }
}

