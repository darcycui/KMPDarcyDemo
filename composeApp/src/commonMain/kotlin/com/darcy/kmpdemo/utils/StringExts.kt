package com.darcy.kmpdemo.utils

const val DOT =  "."

fun String.suffix(): String {
    if (this.isEmpty()
        || this.contains(DOT).not()
        || this.lastIndexOf(DOT) == this.length - 1
    ) {
        return "unknown"
    }
    return DOT + this.substringAfterLast(DOT)
}