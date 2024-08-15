package dev.pedromalta.stonelinxwrapper.modules.transaction.model

sealed interface BaseTransaction
sealed interface BaseTransactionBuilder {
    fun build(): BaseTransaction
}