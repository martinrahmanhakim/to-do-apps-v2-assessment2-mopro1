package com.d3if3058.assessment2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.d3if3058.assessment2.screen.AboutScreen
import com.d3if3058.assessment2.screen.DetailScreen
import com.d3if3058.assessment2.screen.KEY_ID_TASK
import com.d3if3058.assessment2.screen.MainScreen
import com.d3if3058.assessment2.screen.WelcomeScreen

@Composable
fun SetUpNavGraph(navController: NavHostController = rememberNavController()){
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ){
        composable(route = Screen.Welcome.route){
            WelcomeScreen(navController)
        }
        composable(route = Screen.Home.route){
            MainScreen(navController)
        }
        composable(route = Screen.About.route){
            AboutScreen(navController)
        }
        composable(route = Screen.FormBaru.route){
            DetailScreen(navController)
        }
        composable(
            route = Screen.FormUbah.route,
            arguments = listOf(
                navArgument(KEY_ID_TASK){ type = NavType.LongType }
            )
        ) {navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_TASK)
            DetailScreen(navController, id)
        }
    }
}