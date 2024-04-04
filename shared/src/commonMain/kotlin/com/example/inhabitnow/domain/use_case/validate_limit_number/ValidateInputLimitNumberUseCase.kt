package com.example.inhabitnow.domain.use_case.validate_limit_number

interface ValidateInputLimitNumberUseCase {
    operator fun invoke(input: String): Boolean
}