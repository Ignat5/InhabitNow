package com.example.inhabitnow

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform