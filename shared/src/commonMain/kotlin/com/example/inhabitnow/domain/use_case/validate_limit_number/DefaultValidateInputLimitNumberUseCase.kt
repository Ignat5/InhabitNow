package com.example.inhabitnow.domain.use_case.validate_limit_number

import com.example.inhabitnow.domain.util.DomainConst

class DefaultValidateInputLimitNumberUseCase : ValidateInputLimitNumberUseCase {

    override operator fun invoke(input: String): Boolean {
        return input.toDoubleOrNull()
            ?.let { it in DomainConst.MIN_LIMIT_NUMBER..DomainConst.MAX_LIMIT_NUMBER } ?: false
    }

}