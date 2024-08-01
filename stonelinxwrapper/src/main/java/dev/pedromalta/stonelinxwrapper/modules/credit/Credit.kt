package dev.pedromalta.stonelinxwrapper.modules.credit

import android.app.Activity
import android.util.Log
import com.linx.dtefmobile.model.FinancialType
import com.linx.dtefmobile.sdk.ConfirmationTransactionBuilder
import com.linx.dtefmobile.sdk.CreditTransactionBuilder
import com.linx.dtefmobile.sdk.MobileSDK
import com.linx.dtefmobile.sdk.TransactionBuilder
import com.linx.dtefmobile.sdk.UICallback

class Credit(
    activity: Activity,
    private val uiCallback: UICallback,
) {

    private val applicationName = activity.packageName
    private val mobileSDK = MobileSDK.init(activity, uiCallback)

    fun transact(
        parameters: Map<String, String>,
        autoConfirm: Boolean,
        onFailure: () -> Unit,
        onSuccess: (nsu: Int) -> Unit
    ) {
        val installment = parameters["installment"]?.toInt() ?: 1
        val amount = parameters["amount"]?.toDouble() ?: 0.0
        val cardNumber = parameters["cardNumber"]
        val financialType = try {
            FinancialType.fromString(parameters["financialType"]) ?: FinancialType.ONE_INSTALMENT
        } catch (e: Exception) {
            FinancialType.ONE_INSTALMENT
        }
        val transactionBuilder: CreditTransactionBuilder = TransactionBuilder.credit()
            .withApplicationName<CreditTransactionBuilder>(applicationName)
            .withInstallment(installment)
            .withInstallmentAmount(amount)
            .withCardNumber(cardNumber)
            .withFinancialType(financialType)

        mobileSDK.executeTransaction(transactionBuilder.build()) { status, transactionResult ->
            if (uiCallback.canceledStatus == 1) {
                onFailure()
            } else {
                if (status) {
                    if (autoConfirm) {
                        confirmTransaction(transactionResult.nsu.toInt()) {
                            onSuccess(transactionResult.nsu.toInt())
                        }
                    } else {
                        onSuccess(transactionResult.nsu.toInt())
                    }
                } else {
                    onFailure()
                }
            }

        }
    }

    fun confirmTransaction(
        nsu: Int,
        onSuccess: () -> Unit
    ) {
        val builder: ConfirmationTransactionBuilder = TransactionBuilder
            .confirm()
            .withNsu(nsu)
        var resultCode = mobileSDK.confirmTransaction(builder.build())
        Log.i("ConfirmTransaction", "confirmTransaction resultCode: $resultCode")

        // toda transacao autorizada, apos ser confirmada ou desfeira, deve executar
        // o finalizeTransaction
        resultCode = mobileSDK.finalizeTransaction()
        Log.i("ConfirmTransaction", "finalizeTransaction resultCode: $resultCode")
        onSuccess()
    }

}