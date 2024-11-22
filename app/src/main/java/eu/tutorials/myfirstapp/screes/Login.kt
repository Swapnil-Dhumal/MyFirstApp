package eu.tutorials.myfirstapp.screes

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import eu.tutorials.myfirstapp.navigation.Routes
import eu.tutorials.myfirstapp.viewModel.AuthViewModel


@Composable
fun Login(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context  = LocalContext.current

    val authViewModel : AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState()
    val error by authViewModel.error.observeAsState()

    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null){
            navController.navigate(Routes.BottomNav.route){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }



    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login" , style = TextStyle(
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp
        )
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(value = email , onValueChange = {email=it},
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ), singleLine = true,

            )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(value = password , onValueChange = {password=it},
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ), singleLine = true,

            )

        Spacer(modifier = Modifier.height(20.dp))

        ElevatedButton(onClick = {

            error?.let{
                Toast.makeText(context,it,Toast.LENGTH_LONG).show()
            }

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(context,"Please provide all fields",Toast.LENGTH_LONG).show()
            }else
                authViewModel.login(email, password, context)

        }) {
            Text("Login" , style = TextStyle(
                fontSize = 20.sp
            ),
                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, start = 92.dp, end = 92.dp)
            )
        }

        Spacer(modifier = Modifier.height(7.dp))

        TextButton(onClick = {
            navController.navigate(Routes.Register.route){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }) {
            Text("New user? create account" , style = TextStyle())
        }
    }
}