package io.github.magonxesp.zerochanclient

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("id") val id: Int,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
    @SerialName("thumbnail") val thumbnail: String,
    @SerialName("source") val source: String,
    @SerialName("tag") val tag: String,
    @SerialName("tags") val tags: List<String>
)
