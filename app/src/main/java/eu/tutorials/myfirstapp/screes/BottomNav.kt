package eu.tutorials.myfirstapp.screes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.tutorials.myfirstapp.model.BottomNavItem
import eu.tutorials.myfirstapp.navigation.Routes

@Composable
fun BottomNav(navController: NavHostController) {

//    it used to  navigate the bottom bar item
    val navController1 = rememberNavController()

    Scaffold(
        bottomBar = {MyBottomBar(navController1)}) {
        NavHost(navController = navController1, startDestination = Routes.Home.route,
            modifier = Modifier.padding(it)
            ) {
            composable(route =  Routes.Home.route){
                Home(navController1)
            }
            composable(route = Routes.Notification.route){
                Notification()
            }
            composable(route = Routes.AddThread.route){
                AddThread(navController1)
            }
            composable(route = Routes.Profile.route){
                Profile(navController1)
            }
            composable(route = Routes.Search.route){
                Search(navController)
            }
        }
    }
}



@Composable
fun MyBottomBar(navController1: NavHostController){

    val backStackEntry = navController1.currentBackStackEntryAsState()

    val list = listOf(
            BottomNavItem(
                title = "Home",
                route = Routes.Home.route,
                icon = Icons.Rounded.Home
            ),
            BottomNavItem(
            title = "Search",
            route = Routes.Search.route,
            icon = Icons.Rounded.Search
            ),

        BottomNavItem(
            title = "Add Thread",
            route = Routes.AddThread.route,
            icon = Icons.Rounded.Add
        ),

        BottomNavItem(
            title = "Notification",
            route = Routes.Notification.route,
            icon = Icons.Rounded.Notifications
        ),

        BottomNavItem(
            title = "Profile",
            route = Routes.Profile.route,
            icon = Icons.Rounded.Person
        )
    )

    BottomAppBar {
         list.forEach {

             val selected = it.route == backStackEntry?.value?.destination?.route

             NavigationBarItem(
                 selected = selected,

                 onClick ={
                     navController1.navigate(it.route){
                         popUpTo(navController1.graph.findStartDestination().id){
                             saveState = true
                         }
                         launchSingleTop = true
                         restoreState = true
                     }
                 },
                 icon = {
                     Icon(imageVector = it.icon , contentDescription = null, modifier = Modifier.size(27.dp))
                 },
             )
         }
    }
}