package com.magonxesp.zerochanclient

import java.lang.Exception

class ZerochanClientException(override val message: String? = null) : Exception(message)