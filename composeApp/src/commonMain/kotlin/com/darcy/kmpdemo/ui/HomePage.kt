package com.darcy.kmpdemo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.darcy.kmpdemo.navigation.Pages
import dev.icerock.moko.resources.compose.stringResource as mokoStringResource

@Composable
fun ShowHome(
    modifier: Modifier = Modifier,
    onNextButtonCLicked: (String) -> Unit = {}
) {
    // darcyRefactor: 可观察的状态列表
    val pagesStateList = remember { mutableStateListOf<Pages>() }
    pagesStateList.apply {
        add(Pages.EncryptTextPage)
        add(Pages.EncryptFilePage)
        add(Pages.LoadResourcePage)
        add(Pages.LoadMokoResourcePage)
        add(Pages.KtorHttpPage)
        add(Pages.KtorWebsocketPage)
        add(Pages.DownloadImagePage)
        add(Pages.UploadImagePage)
        add(Pages.KtorWebSocketSTMOPPage)
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        // 垂直间距
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(pagesStateList.size) { index ->
            HomeItem(onNextButtonCLicked = onNextButtonCLicked, page = pagesStateList[index])
        }
    }
}

@Composable
fun HomeItem(
    onNextButtonCLicked: (String) -> Unit = {}, page: Pages
) {
    Button(modifier = Modifier.fillMaxWidth(), onClick = {
        onNextButtonCLicked(page.name)
    }) {
        Text(text = mokoStringResource(page.title))
    }
}