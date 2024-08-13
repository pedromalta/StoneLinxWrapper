package dev.pedromalta.stonelinxwrapper.view

import android.app.Activity
import android.text.InputType
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.linx.dtefmobile.sdk.InputModel
import com.linx.dtefmobile.sdk.MenuItem
import com.linx.dtefmobile.sdk.MenuResult
import com.linx.dtefmobile.sdk.Result
import com.linx.dtefmobile.sdk.UICallback


class DefaultUiCallback(private val activity: Activity) : UICallback {

    companion object {
        const val TAG = "DefaultUiCallback"
    }

    private var canceled = false


    override fun onBeep() {
        Log.e(TAG, "onBeep")
    }

    override fun onClean() {
        Log.e(TAG, "onClean")
    }

    override fun onShowError(error: String) {
        activity.runOnUiThread {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Error")
            builder.setMessage(error)
            builder.setPositiveButton("OK") { _, _ -> }
            builder.show()
        }
    }

    override fun onShowMessage(message: String) {
        activity.runOnUiThread {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Message")
            builder.setMessage(message)
            builder.setPositiveButton("OK") { _, _ -> }
            builder.show()
        }
    }

    override fun requestConfirmation(message: String, result: Result) {
        activity.runOnUiThread {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(message)

            // Set up the buttons
            builder.setPositiveButton(
                "OK"
            ) { dialog, _ ->
                result.success("")
                dialog.dismiss()
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog, _ ->
                result.canceled()
                dialog.cancel()
            }

            builder.show()
        }
    }

    override fun onShowAlert(alert: String?) {
        activity.runOnUiThread {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Alert")
            builder.setMessage(alert)
            builder.setPositiveButton("OK") { _, _ -> }
            builder.show()
        }
    }

    override fun onInput(inputModel: InputModel<*>, result: Result) {
        activity.runOnUiThread {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(inputModel.label)

            // Set up the input
            val input = EditText(activity)

            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.inputType = InputType.TYPE_CLASS_NUMBER
            builder.setView(input)


            // Set up the buttons
            builder.setPositiveButton(
                "OK"
            ) { dialog, _ ->
                result.success(input.text.toString())
                dialog.dismiss()
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog, _ ->
                result.canceled()
                dialog.cancel()
            }

            builder.show()
        }
    }

    override fun onShowMenu(
        p0: String,
        p1: MutableList<MenuItem>,
        p2: Int,
        p3: MenuResult?
    ) {
        Log.e(TAG, "onShowMenu: $p0")
    }

    override fun onMaskInput(
        p0: Boolean,
        p1: String?,
        p2: String?,
        p3: String?,
        p4: Boolean,
        p5: Result?
    ) {
        Log.e(TAG, "onMaskInput: $p0")
    }

    override fun getCanceledStatus(): Int {
        Log.e(TAG, "getCanceledStatus")
        return if (!canceled) 0  else 1
    }

    override fun setCanceled(cancel: Boolean): Int {
        Log.e(TAG, "setCanceled: $cancel")
        canceled = cancel
        return 0
    }

}