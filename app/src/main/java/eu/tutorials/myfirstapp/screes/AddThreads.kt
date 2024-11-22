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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import eu.tutorials.myfirstapp.R
import eu.tutorials.myfirstapp.navigation.Routes
import eu.tutorials.myfirstapp.utils.SharedPref
import eu.tutorials.myfirstapp.viewModel.AddViewThreadModel
import eu.tutorials.myfirstapp.viewModel.AuthViewModel

@Composable
fun AddThread(navController: NavHostController) {

    val threadViewModel : AddViewThreadModel = viewModel()
    val isPosted by threadViewModel.isPosted.observeAsState(false)

    val context = LocalContext.current

    var thread by remember { mutableStateOf("") }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val authViewModel : AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        Manifest.permission.READ_MEDIA_IMAGES
    }else
        Manifest.permission.READ_EXTERNAL_STORAGE

    var launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            uri: Uri? ->
        imageUri = uri
    }

    var permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission() ) {
            isGranter : Boolean ->
        if (isGranter){

        }else{

        }
    }

    LaunchedEffect(isPosted) {
        if(isPosted!!){
            thread =""
            imageUri = null

            navController.navigate(Routes.Home.route){
                popUpTo(Routes.AddThread.route){
                    inclusive = true
                }
            }

        }
    }


    ConstraintLayout(modifier = Modifier .fillMaxSize() .padding(16.dp)) {
        val (crossPic, text,logo,userName,editText,
            attachMedia,replYText,button,imageBox,divider) = createRefs()

        Image(painter = painterResource(id = R.drawable.baseline_close_24),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(crossPic) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .clickable {
                    navController.navigate(Routes.Home.route){
                        popUpTo(Routes.AddThread.route){
                            inclusive = true
                        }
                    }
                })

        Text(
            text = "Add Thread" , style = TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            ), modifier = Modifier.constrainAs(text){
                top.linkTo(crossPic.top)
                start.linkTo(crossPic.end , margin = 12.dp)
                bottom.linkTo(crossPic.bottom)
            }
        )

        Spacer(modifier = Modifier.height(5.dp))

        Image(painter = painterResource(R.drawable.man),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(logo){
                    top.linkTo(text.bottom, margin = 15.dp)
                    start.linkTo(parent.start)
                }.size(36.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Text(
            text = SharedPref.getUserName(context) , style = TextStyle(
                fontSize = 20.sp
            ), modifier = Modifier.constrainAs(userName){
                top.linkTo(logo.top)
                start.linkTo(logo.end , margin = 12.dp)
                bottom.linkTo(logo.bottom)
            }
        )

        BasicTextFieldWithHint(hint = "Start a thread ..." , value =thread ,
            onValueChange = {thread = it}, modifier =Modifier.constrainAs(editText){
            top.linkTo(userName.bottom, margin = 15.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
        }. padding(horizontal = 8.dp, vertical = 8.dp).fillMaxWidth().height(50.dp)
        )

        Divider(color = Color.LightGray, thickness = 1.dp , modifier = Modifier.constrainAs(divider) {
            top.linkTo(editText.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        if (imageUri == null){
            Image(painter = painterResource(id = R.drawable.baseline_attachment_24),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(attachMedia) {
                        top.linkTo(divider.bottom)
                        start.linkTo(divider.start)
                    }
                    .clickable {
                        val isGrant = ContextCompat.checkSelfPermission(
                            context, permissionsToRequest
                        ) == PackageManager.PERMISSION_GRANTED

                        if(isGrant){
                            launcher.launch("image/*")
                        }else {
                            permissionLauncher.launch(permissionsToRequest)
                        }
                    }
            )
        }else{
            Box(modifier = Modifier.background(Color.Gray)
                .padding(1.dp).constrainAs(imageBox){
                    top.linkTo(editText.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }.height(220.dp)){
                Image(painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxHeight()
                     ,
                    contentScale = ContentScale.Crop
                )
                Icon(imageVector = Icons.Default.Close, contentDescription = null,
                    modifier = Modifier.align(Alignment.TopEnd).clickable {
                        imageUri = null
                    }
                )
            }
        }

        Text(
            text = "Anyone can reply" , style = TextStyle(
                fontSize = 20.sp
            ), modifier = Modifier.constrainAs(replYText){
                start.linkTo(parent.start , margin = 12.dp)
                bottom.linkTo(parent.bottom, margin = 12.dp)
            }
        )

        TextButton(onClick = {
                if(imageUri == null && thread != ""){
                    threadViewModel.saveData(thread,FirebaseAuth.getInstance().currentUser!!.uid, image = "")
                }else if (imageUri != null && thread !=""){
                    threadViewModel.saveImage(thread, FirebaseAuth.getInstance().currentUser!!.uid, imageUri!!)
                }else{
                    Toast.makeText(context,"Create Thread first",Toast.LENGTH_LONG).show()
                }

        }, modifier = Modifier.constrainAs(button){
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }) {
            Text(
                text = "Post" , style = TextStyle(
                    fontSize = 20.sp
                ),
            )
        }

    }
}

@Composable
fun BasicTextFieldWithHint(hint:String, value: String, onValueChange : (String)->Unit,
                           modifier: Modifier
                           ){
    Box(modifier = modifier){
        if(value.isEmpty()){
            Text(text = hint, color = Color.Gray)
        }
        BasicTextField(value = value, onValueChange = onValueChange,
            textStyle = TextStyle.Default.copy(color = Color.Black),
            )
    }
}