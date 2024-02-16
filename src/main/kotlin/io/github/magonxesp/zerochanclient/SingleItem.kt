package io.github.magonxesp.zerochanclient

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SingleItem(
    @SerialName("id") val id: Int,
    @SerialName("small") val small: String,
    @SerialName("medium") val medium: String,
    @SerialName("large") val large: String,
    @SerialName("full") val full: String,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
    @SerialName("size") val size: Int,
    @SerialName("hash") val hash: String,
    @SerialName("source") val source: String,
    @SerialName("primary") val primary: String,
    @SerialName("tags") val tags: List<String>
)