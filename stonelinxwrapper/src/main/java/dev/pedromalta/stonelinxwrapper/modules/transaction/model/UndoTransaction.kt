package dev.pedromalta.stonelinxwrapper.modules.transaction.model

class UndoTransaction private constructor(
    val nsu: Int?,
    val amount: Double?,
    val enablePinpad: Boolean,
    val withCardNumberTypingEnabled: Boolean,
): BaseTransaction {

    class Builder: BaseTransactionBuilder {
        private var nsu: Int? = null
        private var amount: Double? = null
        private var enablePinpad: Boolean = false
        private var withCardNumberTypingEnabled: Boolean = false

        fun nsu(nsu: Int?) = apply { this.nsu = nsu }
        fun amount(amount: Double?) = apply { this.amount = amount }
        fun enablePinpad(enablePinpad: Boolean) = apply { this.enablePinpad = enablePinpad }
        fun withCardNumberTypingEnabled(withCardNumberTypingEnabled: Boolean) = apply {
            this.withCardNumberTypingEnabled = withCardNumberTypingEnabled
        }
        override fun build(): UndoTransaction {
            return UndoTransaction(
                nsu = nsu,
                amount = amount,
                enablePinpad = enablePinpad,
                withCardNumberTypingEnabled = withCardNumberTypingEnabled,
            )
        }
    }
}