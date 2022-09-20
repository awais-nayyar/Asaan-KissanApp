package com.example.asaankissan.models

import java.io.Serializable

data class Ad(
    var adId: String = "",
    val adImage: ArrayList<String> = ArrayList(),
    val adTitle: String = "",
    val adDescription: String = "",
    val adPrice: Double = 0.0,
    val adMeasure: String = "",
    val adLocation: String = "",
    val adAddress: String = "",
    val adUserId: String = "",
    val adCategory: String = "",
    val adContactNumber: String = "",
    val adUploadTime: String = "",
    val adStatus : String = ""

) :Serializable

