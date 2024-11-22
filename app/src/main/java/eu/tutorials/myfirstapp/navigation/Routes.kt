package eu.tutorials.myfirstapp.navigation

sealed class Routes(var route : String) {


    object Home: Routes("home")
    object Notification : Routes("notification")
    object Profile : Routes("profile")
    object Search : Routes("search")
    object Splash : Routes("splash")
    object AddThread : Routes("addThread")
    object BottomNav : Routes("bottom_nav")
    object Login : Routes("login")
    object Register : Routes("register")
    object OtherUsers : Routes("other_users/{data}")
}