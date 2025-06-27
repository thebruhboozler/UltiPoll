package com.ultipoll.dataclasses

data class FileDescription(
    var path: String,
    var mode: String,
    var type: String,
    var sha: String,
    var size: Int,
    var url: String
)
