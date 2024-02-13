package com.magonxesp.zerochanclient

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemsResponse(
    @SerialName("items") val items: List<Item>
)