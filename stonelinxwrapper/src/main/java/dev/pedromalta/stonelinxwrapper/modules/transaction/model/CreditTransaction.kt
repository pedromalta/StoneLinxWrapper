package dev.pedromalta.stonelinxwrapper.modules.transaction.model

import com.linx.dtefmobile.model.FinancialType

class CreditTransaction private constructor(
    val installmentAmount: Double?,
    val installment: Int?,
    val cardNumber: String?,
    val enablePinpad: Boolean,
    val financialType: FinancialType
): BaseTransaction {

    class Builder {
        private var installmentAmount: Double? = null
        private var installment: Int? = null
        private var cardNumber: String? = null
        private var enablePinpad: Boolean = false
        private var financialType: FinancialType = FinancialType.ONE_INSTALMENT

        fun installmentAmount(installmentAmount: Double) = apply { this.installmentAmount = installmentAmount }
        fun installment(installment: Int) = apply { this.installment = installment }
        fun cardNumber(cardNumber: String) = apply { this.cardNumber = cardNumber }
        fun enablePinpad(enablePinpad: Boolean) = apply { this.enablePinpad = enablePinpad }
        fun financialType(financialType: FinancialType) = apply { this.financialType = financialType }

        fun build(): CreditTransaction {
            return CreditTransaction(
                installmentAmount,
                installment,
                cardNumber,
                enablePinpad,
                financialType
            )
        }
    }
}