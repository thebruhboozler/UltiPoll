package com.ultipoll.dataclasses

data class TreeResponse(
    val sha: String,
    val url: String,
    val tree: List<FileDescription>
)

