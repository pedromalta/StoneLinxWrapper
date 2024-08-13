package dev.pedromalta.stonelinxwrapper.modules.transaction.react

import android.app.Activity
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.linx.dtefmobile.model.FinancialType
import dev.pedromalta.stonelinxwrapper.helpers.GetFullModuleName
import dev.pedromalta.stonelinxwrapper.helpers.sendEvent
import dev.pedromalta.stonelinxwrapper.modules.transaction.Transaction
import dev.pedromalta.stonelinxwrapper.modules.transaction.model.CreditTransaction
import dev.pedromalta.stonelinxwrapper.modules.uicalback.EventDrivenUiCallback

class TransactionModule(
    private val reactContext: ReactApplicationContext,
) : ReactContextBaseJavaModule(reactContext) {


    companion object {
        private var transaction: Transaction? = null
        private var eventDrivenUiCallback: EventDrivenUiCallback? = null
        private fun clear() {
            transaction = null
            eventDrivenUiCallback = null
        }
    }

    override fun getName(): String {
        return GetFullModuleName("Transaction")
    }

    @ReactMethod
    fun creditTransact(
        autoConfirm: Boolean,
        enablePinpad: Boolean,
        installmentAmount: String,
        installment: String,
        cardNumber: String,
        financialType: String,
        onCallback: Callback,
    ) {
        val activity: Activity = currentActivity ?: run {
            onCallback.invoke(true, "Activity is null")
            return
        }

        val transaction = transaction ?: run {
            val uiCallback = EventDrivenUiCallback(reactContext::sendEvent)
            Transaction(activity, uiCallback.uiCallback)
                .also {
                    transaction = it
                    eventDrivenUiCallback = uiCallback
                }
        }

        transaction.transact(
            CreditTransaction.Builder().apply {
                if (installmentAmount.isNotBlank()) {
                    installmentAmount(installmentAmount.toDouble())
                }
                if (installment.isNotBlank()) {
                    installment(installment.toInt())
                }
                if (cardNumber.isNotBlank()) {
                    cardNumber(cardNumber)
                }
                enablePinpad(enablePinpad)
                financialType(FinancialType.fromString(financialType))
            }.build(),
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
        val credit = transaction ?: run {
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
        val credit = transaction ?: run {
            onCallback.invoke(true, "Credit instance is null")
            return
        }

        credit.finalizeTransaction()
        onCallback(false, "Transaction finalized")
        clear()

    }

    @ReactMethod
    fun getRunningStatus(): Boolean {
        val uiCallback = transaction?.uiCallback
        return uiCallback != null && uiCallback.canceledStatus != 1
    }

    @ReactMethod
    fun cancel() {
        transaction?.uiCallback?.setCanceled(true)
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
