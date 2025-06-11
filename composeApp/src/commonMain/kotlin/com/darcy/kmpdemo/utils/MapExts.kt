package com.darcy.kmpdemo.utils

import kotlin.collections.joinToString

fun Map<String, String>.toUrlEncodedString(): String {
    return this.entries.joinToString("&") { (key,value)->
        "$key=$value"
    }
}