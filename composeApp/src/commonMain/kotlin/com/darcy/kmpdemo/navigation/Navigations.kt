package com.darcy.kmpdemo.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.darcy.kmpdemo.ui.ShowDownloadImage
import com.darcy.kmpdemo.ui.ShowEncryptFile
import com.darcy.kmpdemo.ui.ShowEncryptText
import com.darcy.kmpdemo.ui.ShowHome
import com.darcy.kmpdemo.ui.ShowKtorHttp
import com.darcy.kmpdemo.ui.ShowKtorWebsocket
import com.darcy.kmpdemo.ui.ShowLoadMokoResource
import com.darcy.kmpdemo.ui.ShowLoadResource
import com.darcy.kmpdemo.platform.ShowUploadImage

@Composable
fun AppNavigation(
    // Creates the NavController
    navController: NavHostController = rememberNavController()
) {
    // backstack state
    val backStackEntry = navController.currentBackStackEntryAsState()
    // current screen
    val currentScreen =
        Pages.valueOf(backStackEntry.value?.destination?.route ?: Pages.HomePage.name)

    Scaffold(
        topBar = {
            AppBar(
                currentScreen = currentScreen,
                // show back button
                canNavigateBack = navController.previousBackStackEntry != null,
                // navigate up
                navigateUp = navController::navigateUp
            )
        }
    ) { innerPadding ->
        // Creates the NavHost with the navigation graph consisting of supplied destinations
        NavHost(
            navController = navController,
            startDestination = Pages.HomePage.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Pages.HomePage.name) {
                ShowHome(modifier = Modifier.padding(innerPadding)) {
                    when (it) {
                        Pages.HomePage.name,
                        Pages.EncryptTextPage.name,
                        Pages.EncryptFilePage.name,
                        Pages.LoadResourcePage.name,
                        Pages.LoadMokoResourcePage.name,
                        Pages.KtorHttpPage.name,
                        Pages.KtorWebsocketPage.name,
                        Pages.DownloadImagePage.name,
                        Pages.UploadImagePage.name,
                            -> navController.navigate(it)
                    }
                }
            }
            composable(route = Pages.EncryptTextPage.name) {
                ShowEncryptText()
            }
            composable(route = Pages.EncryptFilePage.name) {
                ShowEncryptFile()
            }
            composable(route = Pages.LoadResourcePage.name) {
                ShowLoadResource()
            }
            composable(route = Pages.LoadMokoResourcePage.name) {
                ShowLoadMokoResource()
            }
            composable(route = Pages.KtorHttpPage.name) {
                ShowKtorHttp()
            }
            composable(route = Pages.KtorWebsocketPage.name) {
                ShowKtorWebsocket()
            }
            composable(route = Pages.DownloadImagePage.name) {
                ShowDownloadImage()
            }
            composable(route = Pages.UploadImagePage.name) {
                ShowUploadImage()
            }
        }
    }
}