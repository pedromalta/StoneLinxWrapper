package dev.pedromalta.stonelinxwrapper.modules.transaction.model

class DebitTransaction private constructor(
    val amount: Double?,
    val cpf: String?,
    val enablePinpad: Boolean,
    val withCardNumberTypingEnabled: Boolean,
): BaseTransaction {

    class Builder: BaseTransactionBuilder {
        private var amount: Double? = null
        private var cpf: String? = null
        private var enablePinpad: Boolean = false
        private var withCardNumberTypingEnabled: Boolean = false

        fun amount(amount: Double) = apply { this.amount = amount }
        fun cpf(cpf: String) = apply { this.cpf = cpf }
        fun enablePinpad(enablePinpad: Boolean) = apply { this.enablePinpad = enablePinpad }
        fun withCardNumberTypingEnabled(withCardNumberTypingEnabled: Boolean) = apply {
            this.withCardNumberTypingEnabled = withCardNumberTypingEnabled
        }
        override fun build(): DebitTransaction {
            return DebitTransaction(
                amount = amount,
                cpf = cpf,
                enablePinpad = enablePinpad,
                withCardNumberTypingEnabled = withCardNumberTypingEnabled,
            )
        }
    }
}