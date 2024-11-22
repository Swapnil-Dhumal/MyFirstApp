package eu.tutorials.myfirstapp.screes


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import eu.tutorials.myfirstapp.R
import eu.tutorials.myfirstapp.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun Splash(navController: NavHostController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(R.drawable.logo), contentDescription = null)
    }

    LaunchedEffect(true) {
        delay(2000)

        if(FirebaseAuth.getInstance().currentUser != null)
        navController.navigate(Routes.BottomNav.route){
            popUpTo(navController.graph.startDestinationId)
            launchSingleTop = true
        }
        else
            navController.navigate(Routes.Login.route){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
    }
}