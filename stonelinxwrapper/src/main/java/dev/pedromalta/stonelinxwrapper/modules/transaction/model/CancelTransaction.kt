package dev.pedromalta.stonelinxwrapper.modules.transaction.model

class CancelTransaction private constructor(
    val nsu: Int?,
    val enablePinpad: Boolean,
    val withCardNumberTypingEnabled: Boolean,
): BaseTransaction {

    class Builder: BaseTransactionBuilder {
        private var nsu: Int? = null
        private var enablePinpad: Boolean = false
        private var withCardNumberTypingEnabled: Boolean = false

        fun nsu(nsu: Int?) = apply { this.nsu = nsu }
        fun enablePinpad(enablePinpad: Boolean) = apply { this.enablePinpad = enablePinpad }
        fun withCardNumberTypingEnabled(withCardNumberTypingEnabled: Boolean) = apply {
            this.withCardNumberTypingEnabled = withCardNumberTypingEnabled
        }
        override fun build(): CancelTransaction {
            return CancelTransaction(
                nsu = nsu,
                enablePinpad = enablePinpad,
                withCardNumberTypingEnabled = withCardNumberTypingEnabled,
            )
        }
    }
}
