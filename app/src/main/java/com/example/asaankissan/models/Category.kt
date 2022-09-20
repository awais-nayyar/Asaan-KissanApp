package com.example.asaankissan.models

import de.hdodenhof.circleimageview.CircleImageView
import java.io.Serializable
import java.util.*

data class Category(
    val catId : String = "",
    val catImage : String = "",
    val catName : String = ""
): Serializable