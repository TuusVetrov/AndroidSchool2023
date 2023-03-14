package com.example.exercise2.domain.model

data class AuthorItem(
    val name: String,
    val books: List<Pair<String, Boolean>>
)