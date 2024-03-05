package com.example.inhabitnow.domain.record

import com.example.inhabitnow.data.repository.record.RecordRepository

class DefaultRecordDomain(
    private val recordRepository: RecordRepository
): RecordDomain {

}