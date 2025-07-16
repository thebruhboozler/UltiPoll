package com.ultipoll.dataclasses


data class PollResult (
    val winner: String,
    val participant: Int,
    val title: String
)