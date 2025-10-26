package com.example.keyguard.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.keyguard.R
import com.example.keyguard.security.AuthResult
import com.example.keyguard.security.Biometrica
import com.example.keyguard.ui.theme.KeyGuardTheme
import com.example.keyguard.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.keyguardtest),
            contentDescription = "Logo app",
            modifier = Modifier
                .fillMaxWidth(0.7f) // Usamos una fracción del ancho para que no sea tan grande
                .height(150.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(32.dp))


        Button(onClick = {

            val activity = context as? FragmentActivity


            if (activity != null) {
                val biometrica = Biometrica(activity)


                biometrica.autenticar { resultado ->

                    when (resultado) {
                        is AuthResult.Exito -> {
                            Toast.makeText(context, "¡Autenticación Exitosa!", Toast.LENGTH_SHORT).show()

                        }
                        is AuthResult.Fallo -> {

                        }
                        is AuthResult.Error -> {

                            Toast.makeText(context, resultado.mensaje, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {

                Toast.makeText(context, "No se pudo iniciar la autenticación.", Toast.LENGTH_LONG).show()
            }
        }) {
            Text("Desbloquear con Huella")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    KeyGuardTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.keyguardtest),
                contentDescription = "Logo app",
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { }) {
                Text("Desbloquear con Huella")
            }
        }
    }
}
