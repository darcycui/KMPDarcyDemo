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
import com.darcy.kmpdemo.ui.ShowEncryptFile
import com.darcy.kmpdemo.ui.ShowEncryptText
import com.darcy.kmpdemo.ui.ShowHome
import com.darcy.kmpdemo.ui.ShowKtorNetwork
import com.darcy.kmpdemo.ui.ShowLoadMokoResource
import com.darcy.kmpdemo.ui.ShowLoadResource

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
                        Pages.HomePage.name -> navController.navigate(Pages.HomePage.name)
                        Pages.EncryptTextPage.name -> navController.navigate(Pages.EncryptTextPage.name)
                        Pages.EncryptFilePage.name -> navController.navigate(Pages.EncryptFilePage.name)
                        Pages.LoadResourcePage.name -> navController.navigate(Pages.LoadResourcePage.name)
                        Pages.LoadMokoResourcePage.name -> navController.navigate(Pages.LoadMokoResourcePage.name)
                        Pages.KtorNetworkPage.name -> navController.navigate(Pages.KtorNetworkPage.name)
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
            composable(route = Pages.KtorNetworkPage.name) {
                ShowKtorNetwork()
            }
        }
    }
}