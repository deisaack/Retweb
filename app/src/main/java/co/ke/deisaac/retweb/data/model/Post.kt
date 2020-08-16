package co.ke.deisaac.retweb.data.model

import com.google.gson.annotations.SerializedName

data class Post(
    val id: Int,
    val userId: String,
    val title: String,
    @SerializedName("body")
    val text: String
)