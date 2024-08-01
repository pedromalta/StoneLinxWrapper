package dev.pedromalta.stonelinxwrapper.modules.auth

import android.content.Context
import com.linx.dtefmobile.model.ParameterType
import com.linx.dtefmobile.sdk.Auth
import com.linx.dtefmobile.sdk.Environment
import com.linx.dtefmobile.sdk.UICallback
import com.linx.mobile.logger.Log
import dev.pedromalta.stonelinxwrapper.modules.auth.model.CredentialInfo


class Authentication(
    context: Context,
    uiCallback: UICallback,
    credentialInfo: CredentialInfo
) {

    private val auth = Auth(context, uiCallback)
    private val credential: Auth.Credential = Auth.AuthCredential.getCredential(
        Environment.SANDBOX,
        credentialInfo.cnpj,
        credentialInfo.accessToken,
    )

    fun authenticate(
        onFailure: () -> Unit,
        onSuccess: (nsu: String) -> Unit,
    ) = auth.authenticate(credential) { success, result ->
        if (success) {
            val nsu = result.getParameter(ParameterType.NSU)
            Log.d("Authentication", "status: true, result nsu: $nsu")
            onSuccess(nsu)
        } else {
            Log.d("Authentication", "status: false")
            onFailure()
        }
    }
}