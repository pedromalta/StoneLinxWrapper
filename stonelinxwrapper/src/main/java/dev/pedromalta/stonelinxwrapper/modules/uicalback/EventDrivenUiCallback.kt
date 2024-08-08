package dev.pedromalta.stonelinxwrapper.modules.uicalback

import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.linx.dtefmobile.sdk.InputModel
import com.linx.dtefmobile.sdk.MenuItem
import com.linx.dtefmobile.sdk.MenuResult
import com.linx.dtefmobile.sdk.Result
import com.linx.dtefmobile.sdk.UICallback
import java.util.UUID

class EventDrivenUiCallback(
    private val reactContext: ReactContext
) {

    companion object {

        private val uiCallbackMap = mutableMapOf<String, EventDrivenUiCallback>()

        fun getUiCallback(uniqueId: String): EventDrivenUiCallback? {
            return uiCallbackMap[uniqueId]
        }
    }

    val uniqueId = UUID.randomUUID().toString()

    var status = Status.RUNNING
        private set
    var result: Result? = null
        private set

    init {
        uiCallbackMap[uniqueId] = this
    }

    private fun sendEvent(reactContext: ReactContext, eventName: String, params: WritableMap?) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }

    fun cancel() {
        status = Status.CANCELED
        uiCallbackMap.remove(uniqueId)
    }

    fun confirmResult(cancel: Boolean = false) {
        if (cancel) {
            result?.canceled()
        } else {
            result?.success("")
        }
    }

    fun returnLastStep() {
        result?.returnLastStep()
    }

    val uiCallback = object: UICallback {
        override fun onBeep() {
            Log.i("LinxUiCallBackEvent", "onBeep")
            sendEvent(
                reactContext,
                "onBeep",
                Arguments.createMap().apply {
                    putString("uniqueId", uniqueId)
                }
            )
        }

        override fun onClean() {
            Log.i("LinxUiCallBackEvent", "onClean")
            sendEvent(
                reactContext,
                "onClean",
                Arguments.createMap().apply {
                    putString("uniqueId", uniqueId)
                }
            )
        }

        override fun onShowError(error: String?) {
            Log.i("LinxUiCallBackEvent", "onShowError: $error")
            sendEvent(
                reactContext,
                "onShowError",
                Arguments.createMap().apply {
                    putString("error", error)
                    putString("uniqueId", uniqueId)
                }
            )
        }

        override fun onShowMessage(message: String?) {
            Log.i("LinxUiCallBackEvent", "onShowMessage: $message")
            sendEvent(
                reactContext,
                "onShowMessage",
                Arguments.createMap().apply {
                    putString("message", message)
                    putString("uniqueId", uniqueId)
                }
            )
        }

        override fun requestConfirmation(message: String?, result: Result?) {
            Log.i("LinxUiCallBackEvent", "requestConfirmation: $message")
            this@EventDrivenUiCallback.result = result
            sendEvent(
                reactContext,
                "onRequestConfirmation",
                Arguments.createMap().apply {
                    putString("message", message)
                    putString("uniqueId", uniqueId)
                }
            )
        }

        override fun onShowAlert(alert: String?) {
            Log.i("LinxUiCallBackEvent", "onShowAlert: $alert")
            sendEvent(
                reactContext,
                "onShowAlert",
                Arguments.createMap().apply {
                    putString("alert", alert)
                    putString("uniqueId", uniqueId)
                }
            )
        }

        override fun onInput(input: InputModel<*>?, result: Result?) {
            Log.i("LinxUiCallBackEvent", "onShowInput: $result")
        }

        override fun onShowMenu(
            message: String?,
            menuItem: MutableList<MenuItem>?,
            p2: Int,
            p3: MenuResult?
        ) {
            TODO("Not yet implemented")
        }

        override fun onMaskInput(
            p0: Boolean,
            p1: String?,
            p2: String?,
            p3: String?,
            p4: Boolean,
            p5: Result?
        ) {
            TODO("Not yet implemented")
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