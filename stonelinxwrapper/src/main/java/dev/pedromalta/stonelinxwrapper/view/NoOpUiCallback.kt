package dev.pedromalta.stonelinxwrapper.view

import android.util.Log
import com.linx.dtefmobile.sdk.InputModel
import com.linx.dtefmobile.sdk.MenuItem
import com.linx.dtefmobile.sdk.MenuResult
import com.linx.dtefmobile.sdk.Result
import com.linx.dtefmobile.sdk.UICallback

class NoOpUiCallback: UICallback {

    private var canceled = false

    override fun onBeep() {
        Log.e("StoneLinxWrapperAuthModule", "onBeep")
    }

    override fun onClean() {
        Log.e("StoneLinxWrapperAuthModule", "onClean")
    }

    override fun onShowError(error: String) {
        Log.e("StoneLinxWrapperAuthModule", "onShowError: $error")
    }

    override fun onShowMessage(message: String) {
        Log.e("StoneLinxWrapperAuthModule", "onShowMessage: $message")
    }

    override fun requestConfirmation(message: String, result: Result) {
        Log.e("StoneLinxWrapperAuthModule", "requestConfirmation: $message")
    }

    override fun onShowAlert(alert: String) {
        Log.e("StoneLinxWrapperAuthModule", "onShowAlert: $alert")
    }

    override fun onInput(input: InputModel<*>, result: Result) {
        Log.e("StoneLinxWrapperAuthModule", "onShowInput: $input")
    }

    override fun onShowMenu(
        p0: String,
        p1: MutableList<MenuItem>,
        p2: Int,
        p3: MenuResult?
    ) {
        Log.e("StoneLinxWrapperAuthModule", "onShowMenu: $p0")
    }

    override fun onMaskInput(
        p0: Boolean,
        p1: String?,
        p2: String?,
        p3: String?,
        p4: Boolean,
        p5: Result?
    ) {
        Log.e("StoneLinxWrapperAuthModule", "onMaskInput: $p0")
    }

    override fun getCanceledStatus(): Int {
        Log.e("StoneLinxWrapperAuthModule", "getCanceledStatus")
        return if (!canceled) 0  else 1
    }

    override fun setCanceled(cancel: Boolean): Int {
        Log.e("StoneLinxWrapperAuthModule", "setCanceled: $cancel")
        canceled = cancel
        return 0
    }
}