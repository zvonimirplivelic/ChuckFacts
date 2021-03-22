package com.zvonimirplivelic.chuckfacts.model


import com.google.gson.annotations.SerializedName

data class ChuckFact(
//    @SerializedName("categories")
//    val categories: List<Categories>,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("updated_at")
    val updatedAt: String,
//    @SerializedName("url")
//    val url: String,
    @SerializedName("value")
    val value: String
)