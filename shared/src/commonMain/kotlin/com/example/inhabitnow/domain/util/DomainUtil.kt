package com.example.inhabitnow.domain.util

object DomainUtil {
    fun validateInputLimitNumber(input: String): Boolean {
        return input.toDoubleOrNull()
            ?.let { it in DomainConst.MIN_LIMIT_NUMBER..DomainConst.MAX_LIMIT_NUMBER } ?: false
    }
}