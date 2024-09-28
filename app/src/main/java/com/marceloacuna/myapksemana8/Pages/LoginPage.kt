package com.marceloacuna.myapksemana8.Pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.marceloacuna.myapksemana8.AuthState
import com.marceloacuna.myapksemana8.AuthViewModel
import com.marceloacuna.myapksemana8.R
import com.marceloacuna.myapksemana8.Routes

@Composable
fun Login(modifier: Modifier = Modifier,navController: NavController,authViewModel: AuthViewModel) {

    //se declara variable para capturar los datos ingresados
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate(Routes.home)
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message,Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )

    {
        //imagen de pantalla de inicio
        Image(painter = painterResource(id = R.drawable.icono_mano), contentDescription = "Login Image",
            modifier = Modifier.size(200.dp))

        //texto de pantalla de inicio y su estilo
        Text(text = "Bienvenido My App", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        //salto de linea
        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Ingrese con su cuenta")

        //salto de linea
        Spacer(modifier = Modifier.height(16.dp))

        //Input para ingresar el email
        OutlinedTextField(value = email, onValueChange = { email = it}, label = {Text(text = "Email") },shape = RoundedCornerShape(50))

        //salto de linea
        Spacer(modifier = Modifier.height(16.dp))

        //Input para ingresar el Contraseña
        OutlinedTextField(value = password, onValueChange = {password = it}, label = {Text(text = "Contraseña")
        },shape = RoundedCornerShape(50), visualTransformation = PasswordVisualTransformation())


        //salto de linea
        Spacer(modifier = Modifier.height(16.dp))


        //boton Login
        Button(onClick = {authViewModel.login(email,password)},
            enabled = authState.value != AuthState.Loading)

        {
            Text(text = "Login",modifier = Modifier.height(20.dp).width(100.dp),textAlign = TextAlign.Center)
        }

        //salto de linea
        Spacer(modifier = Modifier.height(32.dp))

        //registrarse
        Text(text = "No tienes una cuenta?, Registrarse", modifier = Modifier.clickable {
            navController.navigate(Routes.Register)
        })

        //salto de linea
        Spacer(modifier = Modifier.height(20.dp))

        //recuperar contraseña
        Text(text = "Recuperar Contraseña?", modifier = Modifier.clickable {/*codigo que realiza la accion*/})

        //salto de linea
        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "o iniciar seción con")


        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        )

        {

            //imagen de registro -> google
            Image(painter = painterResource(id = R.drawable.logo_google1), contentDescription = "google Image",
                modifier = Modifier
                    .size(50.dp)
                    .clickable { /*se agrega codigo al hacer click*/ })


            //imagen de registro -> facebook
            Image(painter = painterResource(id = R.drawable.logo_facebook), contentDescription = "facebook Image",
                modifier = Modifier
                    .size(45.dp)
                    .clickable { /*se agrega codigo al hacer click*/ })


            //imagen de registro -> twiter
            Image(painter = painterResource(id = R.drawable.logo_twiter1), contentDescription = "twiter Image",
                modifier = Modifier
                    .size(50.dp)
                    .clickable { /*se agrega codigo al hacer click*/ })

        }
    }
}