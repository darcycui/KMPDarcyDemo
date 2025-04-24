package com.darcy.kmpdemo

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.darcy.kmpdemo.ui.EncryptString
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        EncryptString()
    }
}