package eu.tutorials.myfirstapp.screes

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import eu.tutorials.myfirstapp.item_view.ThreadItem
import eu.tutorials.myfirstapp.viewModel.HomeViewModel
import kotlin.concurrent.thread

@Composable
fun Home(navController: NavHostController) {

    val context = LocalContext.current
    val homeViewModel : HomeViewModel = viewModel()
    val threadAndUser by homeViewModel.thredsAndUser.observeAsState()

    LazyColumn {
        items(threadAndUser ?: emptyList()) { pairs ->
            ThreadItem(
                thread = pairs.first,
                users = pairs.second,
                navController = navController,
                FirebaseAuth.getInstance().currentUser!!.uid
            )
        }
    }
}