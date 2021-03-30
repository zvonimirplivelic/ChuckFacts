package com.zvonimirplivelic.chuckfacts.model


import com.google.gson.annotations.SerializedName

data class ChuckFactList(
    @SerializedName("result")
    val result: List<Result>,
    @SerializedName("total")
    val total: Int
) {
    data class Result(
        @SerializedName("categories")
        val categories: List<Any>,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("icon_url")
        val iconUrl: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("value")
        val value: String
    )
}