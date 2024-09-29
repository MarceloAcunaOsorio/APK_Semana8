package com.marceloacuna.myapksemana8

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState


    init{
        checkAuthStatus()
    }



    //chequea el estatus
    fun checkAuthStatus(){
        if(auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        }
        else{
            _authState.value = AuthState.Unauthenticated
        }
    }


    //login
    fun login(email : String, password : String){

        if(email.isEmpty() || password.isEmpty())
        {
            _authState.value = AuthState.Error("email o password invalidos")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{task->
            if (task.isSuccessful)
            {
                _authState.value = AuthState.Authenticated
            }
            else
            {
                _authState.value = AuthState.Error(task.exception?.message?:"algo salio mal")
            }
        }
    }



    //login
    fun Registrar(nombreCompleto : String ,email : String, password : String){

        if(email.isEmpty())
        {
            _authState.value = AuthState.Error("Email invalido")
            return
        }
        else if(password.isEmpty())
        {
            _authState.value = AuthState.Error("password invalida")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }

    }


    //Cerra Sesion
    fun cerrarSesion(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }




}

//estatus
sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}