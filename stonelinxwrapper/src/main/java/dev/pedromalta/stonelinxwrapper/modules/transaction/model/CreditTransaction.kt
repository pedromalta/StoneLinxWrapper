package dev.pedromalta.stonelinxwrapper.modules.transaction.model

import com.linx.dtefmobile.model.FinancialType

class CreditTransaction private constructor(
    val amount: Double?,
    val cpf: String?,
    val installmentAmount: Double?,
    val installment: Int?,
    val cardNumber: String?,
    val enablePinpad: Boolean,
    val withCardNumberTypingEnabled: Boolean?,
    val financialType: FinancialType
): BaseTransaction {

    class Builder {
        private var amount: Double? = null
        private var cpf: String? = null
        private var installmentAmount: Double? = null
        private var installment: Int? = null
        private var cardNumber: String? = null
        private var enablePinpad: Boolean = false
        private var withCardNumberTypingEnabled: Boolean? = null
        private var financialType: FinancialType = FinancialType.ONE_INSTALMENT

        fun amount(amount: Double) = apply { this.amount = amount }
        fun cpf(cpf: String) = apply { this.cpf = cpf }
        fun installmentAmount(installmentAmount: Double) = apply {
            this.installmentAmount = installmentAmount
        }
        fun installment(installment: Int) = apply { this.installment = installment }
        fun cardNumber(cardNumber: String) = apply { this.cardNumber = cardNumber }
        fun enablePinpad(enablePinpad: Boolean) = apply { this.enablePinpad = enablePinpad }
        fun financialType(financialType: FinancialType) = apply { this.financialType = financialType }
        fun withCardNumberTypingEnabled(withCardNumberTypingEnabled: Boolean) = apply {
            this.withCardNumberTypingEnabled = withCardNumberTypingEnabled
        }

        fun build(): CreditTransaction {
            return CreditTransaction(
                amount = amount,
                cpf = cpf,
                installmentAmount = installmentAmount,
                installment = installment,
                cardNumber = cardNumber,
                enablePinpad = enablePinpad,
                withCardNumberTypingEnabled = withCardNumberTypingEnabled,
                financialType = financialType
            )
        }
    }
}