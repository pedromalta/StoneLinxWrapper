package dev.pedromalta.stonelinxwrapper.modules.transaction.react

import android.app.Activity
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.linx.dtefmobile.model.FinancialType
import com.linx.dtefmobile.sdk.DebitTransactionBuilder
import com.linx.dtefmobile.sdk.TransactionBuilder
import dev.pedromalta.stonelinxwrapper.helpers.GetFullModuleName
import dev.pedromalta.stonelinxwrapper.helpers.sendEvent
import dev.pedromalta.stonelinxwrapper.modules.transaction.Transaction
import dev.pedromalta.stonelinxwrapper.modules.transaction.model.BaseTransaction
import dev.pedromalta.stonelinxwrapper.modules.transaction.model.CreditTransaction
import dev.pedromalta.stonelinxwrapper.modules.transaction.model.DebitTransaction
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
    fun debit(
        autoConfirm: Boolean,
        enablePinpad: Boolean,
        ammount: String,
        cpf: String,
        onCallback: Callback,
    ) {
        val debitTransaction = DebitTransaction.Builder().apply {
            if (ammount.isNotBlank()) {
                ammount(ammount.toDouble())
            }
            if (cpf.isNotBlank()) {
                cpf(cpf)
            }
            enablePinpad(enablePinpad)
        }.build()

        transact(debitTransaction, autoConfirm, onCallback)

    }

    @ReactMethod
    fun credit(
        autoConfirm: Boolean,
        enablePinpad: Boolean,
        installmentAmount: String,
        installment: String,
        cardNumber: String,
        financialType: String,
        onCallback: Callback,
    ) {

        val creditTransaction = CreditTransaction.Builder().apply {
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
        }.build()

        transact(creditTransaction, autoConfirm, onCallback)

    }

    @ReactMethod
    fun confirmTransaction(nsu: String) {
        try {
            transaction?.confirmTransaction(nsu.toInt())
            finalizeTransaction()
        } catch (e: NumberFormatException) {
            eventDrivenUiCallback?.uiCallback?.onShowError("NSU não é um número válido")
        }
    }

    @ReactMethod
    fun finalizeTransaction() {
        transaction?.finalizeTransaction()
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

    private fun transact(baseTransaction: BaseTransaction, autoConfirm: Boolean, onCallback: Callback) {
        val transaction = setTransaction() ?: run {
            onCallback.invoke(true, "Activity is null")
            return
        }

        transaction.transact(
            baseTransaction,
            autoConfirm = autoConfirm,
            onFailure = {
                onCallback.invoke(true)
            },
            onSuccess = { transactionResult ->
                onCallback.invoke(false, transactionResult)
            }
        )
    }

    private fun setTransaction(): Transaction? {
        val activity: Activity = currentActivity ?: run {
            return null
        }
        return transaction ?: run {
            val uiCallback = EventDrivenUiCallback(reactContext::sendEvent)
            Transaction(activity, uiCallback.uiCallback)
                .also {
                    transaction = it
                    eventDrivenUiCallback = uiCallback
                }
        }
    }

}
