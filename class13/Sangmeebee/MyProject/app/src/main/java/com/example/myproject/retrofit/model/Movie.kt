package com.example.myproject.retrofit.model

import com.example.myproject.retrofit.model.Items
import com.google.gson.annotations.SerializedName

data class Movie(

    @SerializedName("lastBuildDate")
    val lastBuildDate: String,
    @SerializedName("total")
    val total: Int,
    @SerializedName("start")
    val start: Int,
    @SerializedName("display")
    val display: Int,
    @SerializedName("items")
    val items: List<Items>
)