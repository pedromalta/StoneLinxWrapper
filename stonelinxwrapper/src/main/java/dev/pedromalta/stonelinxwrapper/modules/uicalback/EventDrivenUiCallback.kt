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

    companion object {
        private const val TAG = "EventDrivenUiCallback"
    }

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
     * Expected Menu from calls for this UiCallback, from here you can select an item and confirm
     */
    var menuResult: MenuResult? = null
        private set

    /**
     * Cancel the UiCallback
     */
    fun cancel() {
        uiCallback.setCanceled(true)
    }

    val uiCallback = object : UICallback {

        private var canceled = false

        override fun onBeep() {
            Log.i(TAG, "onBeep")
            sendEvent(
                "onBeep",
                null
            )
        }

        override fun onClean() {
            Log.i(TAG, "onClean")
            sendEvent(
                "onClean",
                null
            )
        }

        override fun onShowError(error: String) {
            Log.i(TAG, "onShowError: $error")
            sendEvent(
                "onShowError",
                Arguments.createMap().apply {
                    putString("error", error)
                }
            )
        }

        override fun onShowMessage(message: String) {
            Log.i(TAG, "onShowMessage: $message")
            sendEvent(
                "onShowMessage",
                Arguments.createMap().apply {
                    putString("message", message)
                }
            )
        }

        override fun requestConfirmation(message: String, result: Result) {
            Log.i(TAG, "requestConfirmation: $message")
            this@EventDrivenUiCallback.result = result
            sendEvent(
                "onRequestConfirmation",
                Arguments.createMap().apply {
                    putString("message", message)
                }
            )
        }

        override fun onShowAlert(alert: String) {
            Log.i(TAG, "onShowAlert: $alert")
            sendEvent(
                "onShowAlert",
                Arguments.createMap().apply {
                    putString("alert", alert)
                }
            )
        }

        override fun onInput(input: InputModel<*>, result: Result) {
            Log.i(TAG, "onShowInput: $input")
            this@EventDrivenUiCallback.result = result
            this@EventDrivenUiCallback.input = input

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
            title: String,
            list: List<MenuItem>,
            selected: Int,
            menuResult: MenuResult
        ) {
            Log.i(TAG, "onShowMenu: $title")
            this@EventDrivenUiCallback.menuResult = menuResult

            // map objects to react native
            val mappedList = list.map {
                Arguments.createMap().apply {
                    putDouble("code", it.code.toDouble())
                    putString("display", it.display)
                    putString("externalCode", it.externalCode)
                    putString("raw", it.raw)
                }
            }

            val arrayOfMaps = Arguments.fromArray(mappedList.toTypedArray())

            sendEvent(
                "onShowMenu",
                Arguments.createMap().apply {
                    putString("title", title)
                    putDouble("selected", selected.toDouble())
                    putArray("list", arrayOfMaps)
                }
            )
        }

        override fun onMaskInput(
            p0: Boolean,
            p1: String?,
            p2: String?,
            p3: String?,
            p4: Boolean,
            p5: Result?
        ) {
            Log.e(TAG, "onMaskInput: NOT IMPLEMENTED YET!")
            TODO("Not yet implemented")
        }

        override fun getCanceledStatus(): Int {
            Log.e(TAG, "getCanceledStatus")
            return if (!canceled) 0 else 1
        }

        override fun setCanceled(cancel: Boolean): Int {
            Log.e(TAG, "setCanceled: $cancel")
            canceled = cancel
            return 0
        }
    }
}