package com.marceloacuna.myapksemana8.Pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.marceloacuna.myapksemana8.AuthState
import com.marceloacuna.myapksemana8.AuthViewModel
import com.marceloacuna.myapksemana8.R
import com.marceloacuna.myapksemana8.Routes
import java.util.Calendar

@Composable
fun Registrar(modifier: Modifier = Modifier,navController: NavController,authViewModel: AuthViewModel){

    //se declara variable para capturar los datos ingresados
    var nombrecompleto by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmarpassword by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current


    //variables de datepicker
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    var fechanacimiento by remember { mutableStateOf("") }


    LaunchedEffect(authState.value) {
        when(authState.value)
        {
            is AuthState.Authenticated -> navController.navigate(Routes.home)
            is AuthState.Error -> Toast.makeText(context,(authState.value as AuthState.Error).message,Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        //imagen de pantalla de inicio
        Image(painter = painterResource(id = R.drawable.icono_mano), contentDescription = "Login Image",
            modifier = Modifier.size(200.dp))

        //texto de pantalla de inicio y su estilo
        Text(text = "Registro de Usuario", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        //salto de linea
        Spacer(modifier = Modifier.height(4.dp))

        /*//Input para ingresar el Nombre
        OutlinedTextField(value = nombrecompleto, onValueChange = {nombrecompleto = it}, label = {Text(text = "Nombre Completo")},shape = RoundedCornerShape(50))
        */

        //salto de linea
        Spacer(modifier = Modifier.height(4.dp))

        //Input para ingresar el Email
        OutlinedTextField(value = email, onValueChange = {email = it}, label = {Text(text = "Email")},shape = RoundedCornerShape(50))

        //salto de linea
        Spacer(modifier = Modifier.height(4.dp))

        //Input para ingresar el Password
        OutlinedTextField(value = password, onValueChange = {password = it}, label = {Text(text = "Contraseña")},shape = RoundedCornerShape(50))

        //salto de linea
        Spacer(modifier = Modifier.height(4.dp))

        //Input para ingresar el Confirmar Password
        OutlinedTextField(value = confirmarpassword, onValueChange = {confirmarpassword = it}, label = {Text(text = "Confirmar Contraseña")},shape = RoundedCornerShape(50))

        /*//salto de linea
        Spacer(modifier = Modifier.height(4.dp))

        //Input para ingresar Fecha de Nacimiento
        OutlinedTextField(value = fechanacimiento, onValueChange = {fechanacimiento = it}, label = {Text(text = "Fecha de Nacimiento")},shape = RoundedCornerShape(50))

        //salto de linea
        Spacer(modifier = Modifier.height(4.dp))

        //Input para ingresar direccion
        OutlinedTextField(value = direccion, onValueChange = {direccion = it}, label = {Text(text = "Dirección")},shape = RoundedCornerShape(50))
        */
        //salto de linea
        Spacer(modifier = Modifier.height(16.dp))

        //boton Registrar
        Button(onClick = {authViewModel.Registrar(nombrecompleto,email,password,confirmarpassword,fechanacimiento,direccion)},
            enabled = authState.value != AuthState.Loading)
        {
            Text(text="Registrarse",modifier = Modifier.height(20.dp).width(100.dp),textAlign = TextAlign.Center)
        }

        //salto de linea
        Spacer(modifier = Modifier.height(16.dp))

        //volver al login
        Text(text = "Volver al Login", modifier = Modifier.clickable {
            navController.navigate(Routes.Login)
        })

    }
}