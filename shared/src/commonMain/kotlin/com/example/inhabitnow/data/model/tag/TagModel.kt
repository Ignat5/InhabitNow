package com.example.inhabitnow.data.model.tag

import com.example.inhabitnow.core.type.TagColorType

data class TagModel(
    val id: String,
    val title: String,
    val colorType: TagColorType,
    val createdAt: Long
)
