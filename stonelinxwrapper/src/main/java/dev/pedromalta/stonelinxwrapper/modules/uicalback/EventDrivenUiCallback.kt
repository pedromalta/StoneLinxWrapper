package dev.pedromalta.stonelinxwrapper.modules.uicalback

import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.linx.dtefmobile.sdk.InputModel
import com.linx.dtefmobile.sdk.MenuItem
import com.linx.dtefmobile.sdk.MenuResult
import com.linx.dtefmobile.sdk.Result
import com.linx.dtefmobile.sdk.UICallback
import java.math.BigDecimal

/**
 * UiCallback implementation that sends events to React Native
 */
class EventDrivenUiCallback(
    private val sendEvent: (eventName: String, params: WritableMap?) -> Unit,
) {

    /**
     * Status of the UiCallback
     */
    var status = Status.RUNNING
        private set

    /**
     * Result of calls for this UiCallback,
     * from here you can confirm, cancel or return to the last step
     */
    var result: Result? = null
        private set

    /**
     * Expected Inputs from calls for this UiCallback, from here you can fill the input and confirm
     */
    var input: InputModel<*>? = null
        private set

    /**
     * Cancel the UiCallback
     */
    fun cancel() {
        status = Status.CANCELED
    }

    val uiCallback = object: UICallback {
        override fun onBeep() {
            Log.i("LinxUiCallBackEvent", "onBeep")
            sendEvent(
                "onBeep",
                null
            )
        }

        override fun onClean() {
            Log.i("LinxUiCallBackEvent", "onClean")
            sendEvent(
                "onClean",
                null
            )
        }

        override fun onShowError(error: String?) {
            Log.i("LinxUiCallBackEvent", "onShowError: $error")
            sendEvent(
                "onShowError",
                Arguments.createMap().apply {
                    putString("error", error)
                }
            )
        }

        override fun onShowMessage(message: String?) {
            Log.i("LinxUiCallBackEvent", "onShowMessage: $message")
            sendEvent(
                "onShowMessage",
                Arguments.createMap().apply {
                    putString("message", message)
                }
            )
        }

        override fun requestConfirmation(message: String?, result: Result?) {
            Log.i("LinxUiCallBackEvent", "requestConfirmation: $message")
            this@EventDrivenUiCallback.result = result
            sendEvent(
                "onRequestConfirmation",
                Arguments.createMap().apply {
                    putString("message", message)
                }
            )
        }

        override fun onShowAlert(alert: String?) {
            Log.i("LinxUiCallBackEvent", "onShowAlert: $alert")
            sendEvent(
                "onShowAlert",
                Arguments.createMap().apply {
                    putString("alert", alert)
                }
            )
        }

        override fun onInput(input: InputModel<*>?, result: Result?) {
            Log.i("LinxUiCallBackEvent", "onShowInput: $input")
            this@EventDrivenUiCallback.result = result
            this@EventDrivenUiCallback.input = input
            if (input == null) {
                return
            }

            sendEvent(
                "onInput",
                Arguments.createMap().apply {
                    putString("inputType", input.type.name)
                    putString("label", input.label)
                    putString("defaultValue", input.defaultValue)
                    putDouble("minLength", input.minLength.toDouble())
                    putDouble("maxLength", input.maxLength.toDouble())
                    putBoolean("secret", input.isSecret)
                    putBoolean("clearLabel", input.isClearLabel)
                    putDouble("exact", input.exact.toDouble())
                    if (input.type == InputModel.Type.AMOUNT) {
                        putDouble("minValue", (input.minValue as? BigDecimal?)?.toDouble() ?: 0.0)
                        putDouble("maxValue", (input.maxValue as? BigDecimal?)?.toDouble() ?: 0.0)
                    }
                    if (input.type == InputModel.Type.NUMBER) {
                        putDouble("minValue", (input.minValue as? Int?)?.toDouble() ?: 0.0)
                        putDouble("maxValue", (input.maxValue as? Int?)?.toDouble() ?: 0.0)
                    }
                }
            )
        }

        override fun onShowMenu(
            message: String?,
            menuItem: MutableList<MenuItem>?,
            p2: Int,
            p3: MenuResult?
        ) {
            //TODO("Not yet implemented")
        }

        override fun onMaskInput(
            p0: Boolean,
            p1: String?,
            p2: String?,
            p3: String?,
            p4: Boolean,
            p5: Result?
        ) {
            //TODO("Not yet implemented")
        }

        /**
         * Indica que o usuario tem a intencao de abortar a captura assincrona.
         * Esse tipo de interacao e captura, ocorre normalmente no momento da captura de dados do cartao.
         *
         * Ex.: Leitura do cartao e captura da senha
         */
        override fun setCanceled(canceled: Boolean): Int {
            this@EventDrivenUiCallback.cancel()
            return 0
        }

        /**
         * Informa o status da intencao de abortar uma captura assincrona.
         * Esse tipo de interacao e captura, ocorre normalmente no momento da captura de dados do cartao.
         *
         * Ex.: Leitura do cartao e captura da senha
         */
        override fun getCanceledStatus(): Int {
            return when (status) {
                Status.RUNNING -> 0
                Status.CANCELED -> 1
            }
        }
    }

    enum class Status {
        RUNNING,
        CANCELED
    }
}