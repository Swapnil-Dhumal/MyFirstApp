package eu.tutorials.myfirstapp.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.storage
import eu.tutorials.myfirstapp.model.ThreadModel
import eu.tutorials.myfirstapp.model.UserModel
import eu.tutorials.myfirstapp.utils.SharedPref
import java.util.UUID
import kotlin.concurrent.thread

class HomeViewModel : ViewModel(){

    private val db = FirebaseDatabase.getInstance()
    val thread = db.getReference("threads")

    private var _threadsAndUsers = MutableLiveData<List<Pair<ThreadModel,UserModel>>>()
    val thredsAndUser: LiveData<List<Pair<ThreadModel,UserModel>>> = _threadsAndUsers

    init {
        fetchThreadAndUser {
            _threadsAndUsers.value = it
        }
    }

    private fun fetchThreadAndUser(onResult: (List<Pair<ThreadModel, UserModel>>)->Unit){

        thread.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                val result = mutableListOf<Pair<ThreadModel,UserModel>>()
                for (threadSnapshot in snapshot.children){
                    val thread = threadSnapshot.getValue(ThreadModel::class.java)
                    thread.let{
                        fetchUserFromThread(it!!){
                            user ->
                            result.add(0,it to user)

                            if(result.size == snapshot.childrenCount.toInt()){
                                onResult(result)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DatabaseError", "Operation cancelled: ${error.message}", error.toException())
            }
        })
    }

    fun fetchUserFromThread(thread: ThreadModel, onResult: (UserModel)-> Unit){
        db.getReference("users").child(thread.userId)
            .addListenerForSingleValueEvent(object:ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    user?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DatabaseError", "Operation cancelled: ${error.message}", error.toException())
                }

            })
    }


}