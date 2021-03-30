package com.zvonimirplivelic.chuckfacts.model


import com.google.gson.annotations.SerializedName

data class ChuckFactList(
    @SerializedName("result")
    val factList: List<ChuckFact>,
    @SerializedName("total")
    val total: Int
)