package com.example.asaankissan.models
import java.io.Serializable
data class User(
    val id: String = "",
    val name: String = "",
    var phone: String = "",
    val email: String = "",
    val city: String = "",
    val imageUrl: String = ""
) : Serializable
