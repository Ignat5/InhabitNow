package com.example.inhabitnow.core.util

import platform.Foundation.NSUUID

//actual object UUID {
    actual fun randomUUID(): String {
        return NSUUID().UUIDString
    }
//}