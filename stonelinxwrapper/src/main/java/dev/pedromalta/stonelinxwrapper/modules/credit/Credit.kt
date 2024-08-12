package dev.pedromalta.stonelinxwrapper.modules.credit

import android.app.Activity
import android.util.Log
import com.linx.dtefmobile.sdk.ConfirmationTransactionBuilder
import com.linx.dtefmobile.sdk.CreditTransactionBuilder
import com.linx.dtefmobile.sdk.MobileSDK
import com.linx.dtefmobile.sdk.TransactionBuilder
import com.linx.dtefmobile.sdk.UICallback
import dev.pedromalta.stonelinxwrapper.modules.credit.model.CreditTransaction
import dev.pedromalta.stonelinxwrapper.modules.uicalback.EventDrivenUiCallback

class Credit(
    activity: Activity,
    val uiCallback: UICallback,
) {

    private val applicationName = activity.packageName
    private val mobileSDK = MobileSDK.init(activity, uiCallback)

    fun transact(
        creditTransaction: CreditTransaction,
        autoConfirm: Boolean,
        onFailure: () -> Unit,
        onSuccess: (nsu: Int) -> Unit
    ) {
        val transactionBuilder: CreditTransactionBuilder = TransactionBuilder.credit()
            .withApplicationName<CreditTransactionBuilder>(applicationName)
            .withInstallment(creditTransaction.installment)
            .withInstallmentAmount(creditTransaction.installmentAmount)
            .withCardNumber(creditTransaction.cardNumber)
            .withFinancialType(creditTransaction.financialType)
            .withEnablePinpad(creditTransaction.enablePinpad)

        mobileSDK.executeTransaction(transactionBuilder.build()) { status, transactionResult ->
            val nsu = transactionResult.nsu.toInt()
            if (uiCallback.canceledStatus == 1) {
                onFailure()
            } else {
                if (status) {
                    if (autoConfirm) {
                        confirmTransaction(nsu)
                    }
                    onSuccess(nsu)
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