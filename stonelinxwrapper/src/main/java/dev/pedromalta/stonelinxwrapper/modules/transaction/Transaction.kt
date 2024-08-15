package dev.pedromalta.stonelinxwrapper.modules.transaction

import android.app.Activity
import android.util.Log
import com.facebook.react.bridge.ReadableMap
import com.linx.dtefmobile.sdk.ConfirmationTransactionBuilder
import com.linx.dtefmobile.sdk.CreditTransactionBuilder
import com.linx.dtefmobile.sdk.MobileSDK
import com.linx.dtefmobile.sdk.TransactionBuilder
import com.linx.dtefmobile.sdk.UICallback
import dev.pedromalta.stonelinxwrapper.helpers.toReadableMap
import dev.pedromalta.stonelinxwrapper.modules.transaction.model.CreditTransaction

class Transaction(
    activity: Activity,
    val uiCallback: UICallback,
) {

    private val applicationName = activity.packageName
    private val mobileSDK = MobileSDK.init(activity, uiCallback)

    fun transact(
        creditTransaction: CreditTransaction,
        autoConfirm: Boolean,
        onFailure: () -> Unit,
        onSuccess: (transactionResult: ReadableMap) -> Unit
    ) {

        val transactionBuilder: CreditTransactionBuilder = TransactionBuilder.credit()
            .withApplicationName<CreditTransactionBuilder>(applicationName)
            .apply {
                withFinancialType(creditTransaction.financialType)
                if (creditTransaction.installmentAmount != null) {
                    withInstallmentAmount(creditTransaction.installmentAmount)
                }
                if (creditTransaction.installment != null) {
                    withInstallment(creditTransaction.installment)
                }
                if (!creditTransaction.cardNumber.isNullOrEmpty()) {
                    withCardNumber(creditTransaction.cardNumber)
                }
            }
            .withEnablePinpad(creditTransaction.enablePinpad)


        mobileSDK.executeTransaction(transactionBuilder.build()) { status, transactionResult ->
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

}