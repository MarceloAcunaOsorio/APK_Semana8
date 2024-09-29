package com.marceloacuna.myapksemana8.Pages

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.marceloacuna.myapksemana8.AuthState
import com.marceloacuna.myapksemana8.AuthViewModel
import com.marceloacuna.myapksemana8.Model.Letras_Model
import com.marceloacuna.myapksemana8.R
import com.marceloacuna.myapksemana8.Routes



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home (modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    val authState = authViewModel.authState.observeAsState()
    var showMenu by remember { mutableStateOf(false) }
    var context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate(Routes.Login)
            else -> Unit
        }
    }

    TopAppBar(
        title = { Text(text = "My App") },
        colors = TopAppBarDefaults.topAppBarColors(Color.Cyan),
        actions = {

            IconButton(onClick = { Toast.makeText(context, "Home", Toast.LENGTH_SHORT).show() }) {
                Icon(Icons.Default.Home, "")
            }

            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Default.Menu, "")
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                // DropdownMenuItem(text = { Text(text = "Crear Letra")}, onClick = {Toast.makeText(context,"Crear Letra",Toast.LENGTH_SHORT).show()})

                DropdownMenuItem(
                    text = { Text(text = "Cerrar Sesión") },
                    onClick = { authViewModel.cerrarSesion() })
            }
        }
    )
    SpeechRecognitionApp()

    Spacer(modifier = Modifier.height(50.dp))

    crealetra()
}

@Composable
fun SpeechRecognitionApp() {
    val context = LocalContext.current
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    var speechText by remember { mutableStateOf("Presiona el botón y habla") }

    val recognizerIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES") // Español
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            speechRecognizer.startListening(recognizerIntent)
        } else {
            Toast.makeText(context, "Permiso de micrófono denegado", Toast.LENGTH_SHORT).show()
        }
    }

    val recognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {
            speechText = "Escuchando..."
        }

        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {
            speechText = "Procesando..."
        }

        override fun onError(error: Int) {
            speechText = "Error al reconocer"
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            speechText = matches?.firstOrNull() ?: "No se pudo reconocer"
        }

        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    }

    speechRecognizer.setRecognitionListener(recognitionListener)

    // Interfaz de usuario
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paddingFromBaseline(top = 200.dp, bottom = 8.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = speechText,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)

        )
        Button(onClick = {
                // Verificar permiso
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) },
            colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = Color.Yellow)
        ) {
            Text(text = "Presionar y hablar")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SpeechRecognitionApp()
}




/* FUNCION CREAR LETRA*/


@Composable
fun crealetra(){
    var nombreLetra by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var context = LocalContext.current




    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)

    {

        Spacer(modifier = Modifier.height(50.dp))


        //texto de pantalla Crear y su estilo
        androidx.compose.material3.Text(text = "Letra", fontSize = 28.sp,fontWeight = FontWeight.Bold)
        //salto de linea
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = nombreLetra,
            onValueChange = { nombreLetra = it },
            label = { androidx.compose.material.Text("Nombre Letra") },
            modifier = Modifier.size(width = 380.dp, height = 60.dp),
            shape = RoundedCornerShape(40)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { androidx.compose.material.Text("Descripcion Letra") },
            modifier = Modifier.size(width = 380.dp, height = 60.dp),
            shape = RoundedCornerShape(40)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = imagen,
            onValueChange = { imagen = it },
            label = { androidx.compose.material.Text("Imagen") },
            modifier = Modifier.size(width = 380.dp, height = 60.dp),
            shape = RoundedCornerShape(40)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {

            when {
                nombreLetra == "" -> errorMessage = "Nombre no debe quedar vacio"
                descripcion == "" -> errorMessage = "Email no debe quedar vacio"
                imagen == "" -> errorMessage = "Password no debe quedar vacio"

                else -> {
                    val database = FirebaseDatabase.getInstance()
                    val ref = database.getReference("model_abecedario")

                    val model_abecedario = Letras_Model(
                        nombre = nombreLetra,
                        descripcion = descripcion,
                        imgen = imagen
                    )
                    ref.push().setValue(model_abecedario).addOnCompleteListener{task ->
                        if(task.isSuccessful)
                        {
                            Toast.makeText(context, "Letra creada", Toast.LENGTH_SHORT).show()
                        }
                        else
                        {
                            Toast.makeText(context, "Error al crear letra", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        },shape = RoundedCornerShape(16.dp),
            modifier = Modifier.size(width = 280.dp, height = 40.dp)
        ) {
            androidx.compose.material.Text(
                "Crear",
                style = TextStyle(
                    fontSize = 20.sp, // Tamaño de fuente
                    fontWeight = FontWeight.Bold // Peso de la fuente //
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage.isNotEmpty()) {
            androidx.compose.material.Text(text = errorMessage, color = Color.Red)
        }
    }
}


/*obtener listado*/
private lateinit var database : DatabaseReference
private fun obtenerlistadoletras(letra: String){
    database = FirebaseDatabase.getInstance().getReference("model_abecedario")
    val letraRef = database.child(letra)

    letraRef.addListenerForSingleValueEvent(object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.exists()) {
               val letraListadoIndo = snapshot.getValue(Letras_Model::class.java)

                letraListadoIndo?.let { abecedario ->
                    Log.d("listado de letras", "nombre: ${abecedario.nombre}, descripcion : ${abecedario.descripcion}, imagen: ${abecedario.imgen}")
                }?: run{
                    Log.d("listado de letras", "no existe letra")
                }
            }else
            {
                Log.d("listado de letras", "no existe letra")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Error Data Letras", "Error al obtener datos: ${error.message}")
        }
    })
}










