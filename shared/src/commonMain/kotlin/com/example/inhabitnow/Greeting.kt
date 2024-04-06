package com.example.inhabitnow

import kotlinx.datetime.LocalDateTime

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}