package com.example.inhabitnow.core.util

import java.util.UUID

//actual object UUID {
    actual fun randomUUID(): String {
        return UUID.randomUUID().toString()
    }
//}