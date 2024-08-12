package dev.pedromalta.stonelinxwrapper.modules.credit.model

import com.linx.dtefmobile.model.FinancialType

data class CreditTransaction(
    val installmentAmount: Double,
    val installment: Int = 1,
    val cardNumber: String? = null,
    val enablePinpad: Boolean = false,
    val financialType: FinancialType = FinancialType.ONE_INSTALMENT
)