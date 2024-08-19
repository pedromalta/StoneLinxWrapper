package dev.pedromalta.stonelinxwrapper.modules.transaction

import android.app.Activity
import android.util.Log
import com.facebook.react.bridge.ReadableMap
import com.linx.dtefmobile.sdk.ConfirmationTransactionBuilder
import com.linx.dtefmobile.sdk.CreditTransactionBuilder
import com.linx.dtefmobile.sdk.DebitTransactionBuilder
import com.linx.dtefmobile.sdk.MobileSDK
import com.linx.dtefmobile.sdk.Transaction
import com.linx.dtefmobile.sdk.TransactionBuilder
import com.linx.dtefmobile.sdk.UICallback
import dev.pedromalta.stonelinxwrapper.helpers.toReadableMap
import dev.pedromalta.stonelinxwrapper.modules.transaction.model.BaseTransaction
import dev.pedromalta.stonelinxwrapper.modules.transaction.model.BaseTransactionBuilder
import dev.pedromalta.stonelinxwrapper.modules.transaction.model.CreditTransaction
import dev.pedromalta.stonelinxwrapper.modules.transaction.model.DebitTransaction

class Transaction(
    activity: Activity,
    val uiCallback: UICallback,
) {

    private val applicationName = activity.packageName
    private val mobileSDK = MobileSDK.init(activity, uiCallback)

    fun transact(
        transaction: BaseTransaction,
        autoConfirm: Boolean,
        onFailure: () -> Unit,
        onSuccess: (transactionResult: ReadableMap) -> Unit
    ) {
        val transactionBuilder = when (transaction) {
            is CreditTransaction -> {
                buildCreditTransaction(transaction)
            }
            is DebitTransaction -> {
                buildDebitTransaction(transaction)
            }
        }

        transact(transactionBuilder.build(), autoConfirm, onFailure, onSuccess)
    }

    fun transact(
        transaction: Transaction,
        autoConfirm: Boolean,
        onFailure: () -> Unit,
        onSuccess: (transactionResult: ReadableMap) -> Unit
    ) {
        mobileSDK.executeTransaction(transaction) { status, transactionResult ->
            if (uiCallback.canceledStatus == 1) {
                onFailure()
            } else {
                if (status) {
                    val nsu = transactionResult.nsu.toInt()
                    if (autoConfirm) {
                        confirmTransaction(nsu)
                    }
                    onSuccess(transactionResult.toReadableMap())
                } else {
                    onFailure()
                }
            }

        }
    }

    fun confirmTransaction(
        nsu: Int,
    ) {
        val builder: ConfirmationTransactionBuilder = TransactionBuilder
            .confirm()
            .withNsu(nsu)
        Log.i("ConfirmTransaction", "Confirming transaction with NSU: $nsu")
        val resultCode = mobileSDK.confirmTransaction(builder.build())
        Log.i("ConfirmTransaction", "confirmed Transaction resultCode: $resultCode")

        finalizeTransaction()

    }

    fun finalizeTransaction() {
        Log.i("FinalizeTransaction", "Finalizing Transaction")
        val resultCode = mobileSDK.finalizeTransaction()
        Log.i("FinalizeTransaction", "finalizeTransaction resultCode: $resultCode")
    }

    private fun buildCreditTransaction(transaction: CreditTransaction): TransactionBuilder {
        return TransactionBuilder.credit()
            .withApplicationName<CreditTransactionBuilder>(applicationName)
            .apply {
                withFinancialType(transaction.financialType)
                if (transaction.amount != null) {
                    withAmount<CreditTransactionBuilder>(transaction.amount)
                }
                if (transaction.cpf != null) {
                    withCpf<CreditTransactionBuilder>(transaction.cpf)
                }
                if (transaction.installmentAmount != null) {
                    withInstallmentAmount(transaction.installmentAmount)
                }
                if (transaction.installment != null) {
                    withInstallment(transaction.installment)
                }
                if (!transaction.cardNumber.isNullOrEmpty()) {
                    withCardNumber(transaction.cardNumber)
                }
            }
            .withEnablePinpad(transaction.enablePinpad)
    }

    private fun buildDebitTransaction(transaction: DebitTransaction): TransactionBuilder {
        return TransactionBuilder.debit()
            .withApplicationName<DebitTransactionBuilder>(applicationName)
            .apply {
                if (transaction.amount != null) {
                    withAmount<DebitTransactionBuilder>(transaction.amount)
                }
                if (!transaction.cpf.isNullOrEmpty()){
                    withCpf<DebitTransactionBuilder>(transaction.cpf)
                }
                withEnablePinpad<DebitTransactionBuilder>(transaction.enablePinpad)
            }
            .withEnablePinpad(true)
    }

}