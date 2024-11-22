package eu.tutorials.myfirstapp.screes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import eu.tutorials.myfirstapp.R
import eu.tutorials.myfirstapp.item_view.ThreadItem
import eu.tutorials.myfirstapp.model.UserModel
import eu.tutorials.myfirstapp.navigation.Routes
import eu.tutorials.myfirstapp.utils.SharedPref
import eu.tutorials.myfirstapp.viewModel.AuthViewModel
import eu.tutorials.myfirstapp.viewModel.UserViewModel


@Composable
fun OtherUsers(navController: NavHostController, uid: String) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val userViewModel: UserViewModel = viewModel()
    val threads by userViewModel.threads.observeAsState(null)
    val users by userViewModel.users.observeAsState(null)


    userViewModel.fetchThread(uid)
    userViewModel.fetchUser(uid)

    LaunchedEffect(firebaseUser) {
        if(firebaseUser == null){
            navController.navigate(Routes.Login.route){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }



    LazyColumn {
        item {
            ConstraintLayout(modifier = Modifier .fillMaxSize() .padding(16.dp)) {
                val (text, logo, userName, bio, followers, following, button) = createRefs()


                Image(
                    painter = painterResource(R.drawable.man),
                    contentDescription = null,
                    modifier = Modifier
                        .constrainAs(logo) {
                            top.linkTo(parent.top , margin = 5.dp)
                            end.linkTo(parent.end)
                        }.size(50.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = users!!.userName, style = TextStyle(
                        fontSize = 24.sp
                    ), modifier = Modifier.constrainAs(userName) {
                        top.linkTo(text.bottom)
                        start.linkTo(parent.start)
                    }
                )
                Text(
                    text = users!!.bio , style = TextStyle(
                        fontSize = 20.sp
                    ), modifier = Modifier.constrainAs(bio) {
                        top.linkTo(userName.bottom)
                        start.linkTo(parent.start)
                    }
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "0 followers", style = TextStyle(
                        fontSize = 20.sp
                    ), modifier = Modifier.constrainAs(followers) {
                        top.linkTo(bio.bottom)
                        start.linkTo(parent.start)
                    }
                )
                Text(
                    text = "0 following", style = TextStyle(
                        fontSize = 20.sp
                    ), modifier = Modifier.constrainAs(following) {
                        top.linkTo(followers.bottom)
                        start.linkTo(parent.start)
                    }
                )

                if(FirebaseAuth.getInstance().currentUser!!.uid != uid){
                    ElevatedButton(onClick = {

                    }, modifier = Modifier.constrainAs(button){
                        top.linkTo(following.bottom)
                        start.linkTo(parent.start)
                    }

                    ) {
                        Text("Follow")
                    }
                }else{
                    ElevatedButton(onClick = {
                        authViewModel.logout()
                    }, modifier = Modifier.constrainAs(button){
                        top.linkTo(following.bottom)
                        start.linkTo(parent.start)
                    }

                    ) {
                        Text("Logout")
                    }
                }
            }
        }

        if(threads != null && users != null){
            items(threads ?: emptyList()){ pair->
                ThreadItem(
                    thread = pair,
                    users = users!!,
                    navController = navController,
                    userId = SharedPref.getUserName(context),
                )

            }
        }
    }
}