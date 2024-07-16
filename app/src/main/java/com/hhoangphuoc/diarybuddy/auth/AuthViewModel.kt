package com.hhoangphuoc.diarybuddy.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel: ViewModel(){
//    private lateinit var auth: FirebaseAuth;
//// ...
//// Initialize Firebase Auth
//    auth = Firebase.auth


    private var auth : FirebaseAuth? = FirebaseAuth.getInstance()

    //handle the email-password authentication instance, through checking authentication states
    private val _authState = MutableLiveData<ManualAuthState>()
    val authState: MutableLiveData<ManualAuthState> = _authState

    init{
        checkAuthState()
    }

    fun checkAuthState(){
        if(auth?.currentUser == null){
            _authState.value = ManualAuthState.Unauthenticated
        }else{
            _authState.value = ManualAuthState.Authenticated
        }
    }
    fun login(email: String, password: String){

        //checking empty email and password condition
        if(email.isEmpty() || password.isEmpty()){
            _authState.value = ManualAuthState.Error("Email or password cannot be empty")
            return
        }

        _authState.value = ManualAuthState.Loading
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener {
                //add a listener trigger on the state updates
                if(it.isSuccessful){
                    _authState.value = ManualAuthState.Authenticated
                }else{
                    _authState.value = ManualAuthState.Error(it.exception?.message?:"Unknown error")
                }
            }
    }

    fun signup(email: String, password: String){
        //checking empty email and password condition

        if(email.isEmpty() || password.isEmpty()) {
            _authState.value = ManualAuthState.Error("Email or password cannot be empty")
            return
        }
        _authState.value = ManualAuthState.Loading
        auth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener {
                if(it.isSuccessful){
                    _authState.value = ManualAuthState.Authenticated
                }else{
                    _authState.value = ManualAuthState.Error(it.exception?.message?:"Unknown error")
            }
        }
    }
    fun logout(){
        auth?.signOut()
        _authState.value = ManualAuthState.Unauthenticated
    }
}

//Create the state class for handle different authentication state
sealed class ManualAuthState{
//    object Uninitialized: AuthState()
    object Authenticated: ManualAuthState()
    object Unauthenticated: ManualAuthState()
    object Loading: ManualAuthState()

    data class Error(val message: String): ManualAuthState()

}