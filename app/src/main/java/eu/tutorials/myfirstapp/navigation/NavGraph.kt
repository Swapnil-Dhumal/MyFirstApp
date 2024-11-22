package eu.tutorials.myfirstapp.navigation

import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import eu.tutorials.myfirstapp.screes.AddThread
import eu.tutorials.myfirstapp.screes.BottomNav
import eu.tutorials.myfirstapp.screes.Home
import eu.tutorials.myfirstapp.screes.Login
import eu.tutorials.myfirstapp.screes.Notification
import eu.tutorials.myfirstapp.screes.OtherUsers
import eu.tutorials.myfirstapp.screes.Profile
import eu.tutorials.myfirstapp.screes.Register
import eu.tutorials.myfirstapp.screes.Search
import eu.tutorials.myfirstapp.screes.Splash

@Composable
fun NavGraph(navController : NavHostController){
    NavHost(navController = navController, startDestination = Routes.Login.route) {
        composable(Routes.Splash.route){
            Splash(navController)
        }
        composable(Routes.Home.route){
            Home(navController)
        }
        composable(Routes.Notification.route){
            Notification()
        }
        composable(Routes.AddThread.route){
            AddThread(navController)
        }
        composable(Routes.Profile.route){
            Profile(navController)
        }
        composable(Routes.Search.route){
            Search(navController)
        }
        composable(Routes.BottomNav.route) {
            BottomNav(navController)
        }
        composable(Routes.Login.route) {
            Login(navController)
        }
        composable(Routes.Register.route) {
            Register(navController)
        }
        composable(Routes.OtherUsers.route) {
            val data = it.arguments!!.getString("data")
            OtherUsers(navController, data!!)
        }
    }
}