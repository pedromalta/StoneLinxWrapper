package dev.pedromalta.stonelinxwrapper.modules.transaction.model

class DebitTransaction private constructor(
    val ammount: Double?,
    val cpf: String?,
    val enablePinpad: Boolean,
): BaseTransaction {

    class Builder: BaseTransactionBuilder {
        private var ammount: Double? = null
        private var cpf: String? = null
        private var enablePinpad: Boolean = false

        fun ammount(ammount: Double) = apply { this.ammount = ammount }
        fun cpf(cpf: String) = apply { this.cpf = cpf }
        fun enablePinpad(enablePinpad: Boolean) = apply { this.enablePinpad = enablePinpad }

        override fun build(): DebitTransaction {
            return DebitTransaction(
                ammount,
                cpf,
                enablePinpad,
            )
        }
    }
}