package eu.tutorials.myfirstapp.screes

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import eu.tutorials.myfirstapp.R
import eu.tutorials.myfirstapp.navigation.Routes
import eu.tutorials.myfirstapp.viewModel.AuthViewModel


@Composable
fun Register(navController: NavHostController) {
    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val authViewModel : AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val context = LocalContext.current

    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        Manifest.permission.READ_MEDIA_IMAGES
    }else
        Manifest.permission.READ_EXTERNAL_STORAGE

    var launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            uri: Uri? ->
        imageUri = uri
    }

    var permissionLauncher = rememberLauncherForActivityResult(contract =ActivityResultContracts.RequestPermission() ) {
            isGranter : Boolean ->
        if (isGranter){

        }else{

        }
    }

    LaunchedEffect(firebaseUser) {
        if(firebaseUser != null){
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
        Text("Register Here" , style = TextStyle(
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp
        ))

        Spacer(modifier = Modifier.height(12.dp))

        Image(painter = if(imageUri == null) painterResource(id = R.drawable.person)
        else rememberAsyncImagePainter(model = imageUri)
            , contentDescription = null,
            modifier = Modifier.size(96.dp).clip(CircleShape).background(Color.LightGray)
                .clickable {
// when user click on image then they app there profile photo from gallery
                    val isGrant = ContextCompat.checkSelfPermission(
                        context, permissionsToRequest
                    ) == PackageManager.PERMISSION_GRANTED

                    if(isGrant){
                        launcher.launch("image/*")
                    }else {
                        permissionLauncher.launch(permissionsToRequest)
                    }

                },contentScale = ContentScale.Crop)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(value = name , onValueChange = {name=it},
            label = { Text("Name") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ), singleLine = true)

        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(value = username , onValueChange = {username=it},
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ), singleLine = true)

        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(value = bio , onValueChange = {bio=it},
            label = { Text("Bio") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ), singleLine = true)

        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(value = email , onValueChange = {email=it},
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ), singleLine = true)

        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(value = password , onValueChange = {password=it},
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ), singleLine = true)

        Spacer(modifier = Modifier.height(20.dp))

        ElevatedButton(onClick = {
            if(name.isEmpty() || email.isEmpty() || bio.isEmpty() || password.isEmpty() || imageUri == null){
                Toast.makeText(context,"Please fill all details" , Toast.LENGTH_SHORT).show()
            }else{
                authViewModel.register(
                    email, password, name,bio, username, imageUri!!,context)
            }
        }) {
            Text("Register" , style = TextStyle(
                fontSize = 20.sp
            ), modifier = Modifier
                .padding(top = 2.dp, bottom = 2.dp, start = 80.dp, end = 80.dp))
        }

        Spacer(modifier = Modifier.height(7.dp))

        TextButton(onClick = {
//            This tells the app to navigate to the Login screen
            navController.navigate(Routes.Login.route){
//  This removes all screens in the navigation stack up to the start destination,
                popUpTo(navController.graph.startDestinationId)
//  launchSingleTop ==== This ensures that only one instance of the Login screen is in the stack.
                launchSingleTop = true
            }
        } ) {
            Text("Already register? login here" , style = TextStyle())
        }
    }
}
