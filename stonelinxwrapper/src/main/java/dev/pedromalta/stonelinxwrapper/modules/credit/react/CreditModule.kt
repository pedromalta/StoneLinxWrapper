package dev.pedromalta.stonelinxwrapper.modules.credit.react

import android.app.Activity
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.linx.dtefmobile.model.FinancialType
import dev.pedromalta.stonelinxwrapper.helpers.GetFullModuleName
import dev.pedromalta.stonelinxwrapper.helpers.sendEvent
import dev.pedromalta.stonelinxwrapper.modules.credit.Credit
import dev.pedromalta.stonelinxwrapper.modules.credit.model.CreditTransaction
import dev.pedromalta.stonelinxwrapper.modules.uicalback.EventDrivenUiCallback

class CreditModule(
    private val reactContext: ReactApplicationContext,
) : ReactContextBaseJavaModule(reactContext) {


    companion object {
        private var credit: Credit? = null
        private var eventDrivenUiCallback: EventDrivenUiCallback? = null
        private fun clear() {
            credit = null
            eventDrivenUiCallback = null
        }
    }

    override fun getName(): String {
        return GetFullModuleName("Credit")
    }

    @ReactMethod
    fun transact(
        autoConfirm: Boolean,
        enablePinpad: Boolean,
        installmentAmount: Double,
        installment: Double,
        cardNumber: String,
        financialType: String,
        onCallback: Callback,
    ) {
        val activity: Activity = currentActivity ?: run  {
            onCallback.invoke(true, "Activity is null")
            return
        }

        val credit = credit ?: run {
            val uiCallback = EventDrivenUiCallback(reactContext::sendEvent)
            Credit(activity, uiCallback.uiCallback)
                .also {
                    credit = it
                    eventDrivenUiCallback = uiCallback
                }
        }

        credit.transact(
            CreditTransaction(
                installmentAmount = installmentAmount,
                installment = installment.toInt(),
                cardNumber = cardNumber,
                enablePinpad = enablePinpad,
                financialType = FinancialType.fromString(financialType)
            ),
            autoConfirm = autoConfirm,
            onFailure = {
                onCallback.invoke(true)
            },
            onSuccess = { nsu ->
                onCallback.invoke(false, nsu)
            }
        )

    }

    @ReactMethod
    fun confirmTransaction(
        nsu: String,
        onCallback: Callback,
    ) {
        val credit = credit ?: run {
            onCallback.invoke(true, "Credit instance is null")
            return
        }

        credit.confirmTransaction(nsu.toInt())
        credit.finalizeTransaction()

        onCallback(false, "transaction confirmed")

        clear()

    }

    @ReactMethod
    fun finalizeTransaction(
        onCallback: Callback,
    ) {
        val credit = credit ?: run {
            onCallback.invoke(true, "Credit instance is null")
            return
        }

        credit.finalizeTransaction()
        onCallback(false, "Transaction finalized")
        clear()

    }

    @ReactMethod
    fun getRunningStatus(): Boolean {
        val uiCallback = credit?.uiCallback
        return uiCallback != null && uiCallback.canceledStatus != 1
    }

    @ReactMethod
    fun cancel() {
        credit?.uiCallback?.setCanceled(true)
    }

    @ReactMethod
    fun resultSuccess(
        result: String,
    ) {
        eventDrivenUiCallback?.result?.success(result)
    }

    @ReactMethod
    fun resultCanceled() {
        eventDrivenUiCallback?.result?.canceled()
    }

    @ReactMethod
    fun resultReturnLastStep() {
        eventDrivenUiCallback?.result?.returnLastStep()
    }

}
