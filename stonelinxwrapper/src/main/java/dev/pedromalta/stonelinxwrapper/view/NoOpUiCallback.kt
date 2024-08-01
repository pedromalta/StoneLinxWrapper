package dev.pedromalta.stonelinxwrapper.view

import android.util.Log
import com.linx.dtefmobile.sdk.InputModel
import com.linx.dtefmobile.sdk.MenuItem
import com.linx.dtefmobile.sdk.MenuResult
import com.linx.dtefmobile.sdk.Result
import com.linx.dtefmobile.sdk.UICallback

object NoOpUiCallback: UICallback {
    override fun onBeep() {
        Log.e("StoneLinxWrapperAuthModule", "onBeep")
    }

    override fun onClean() {
        Log.e("StoneLinxWrapperAuthModule", "onClean")
    }

    override fun onShowError(p0: String?) {
        Log.e("StoneLinxWrapperAuthModule", "onShowError: $p0")
    }

    override fun onShowMessage(p0: String?) {
        Log.e("StoneLinxWrapperAuthModule", "onShowMessage: $p0")
    }

    override fun requestConfirmation(p0: String?, p1: Result?) {
        Log.e("StoneLinxWrapperAuthModule", "requestConfirmation: $p0")
    }

    override fun onShowAlert(p0: String?) {
        Log.e("StoneLinxWrapperAuthModule", "onShowAlert: $p0")
    }

    override fun onInput(input: InputModel<*>?, result: Result?) {
        Log.e("StoneLinxWrapperAuthModule", "onShowInput: $result")
    }

    override fun onShowMenu(
        p0: String?,
        p1: MutableList<MenuItem>?,
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
        return 0
    }

    override fun setCanceled(p0: Boolean): Int {
        Log.e("StoneLinxWrapperAuthModule", "setCanceled: $p0")
        return 0
    }
}