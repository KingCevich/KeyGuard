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
import com.example.keyguard.viewmodel.MainViewModel
import com.example.keyguard.ui.theme.KeyGuardTheme

@Composable
fun HomeScreen(

    navController: NavController,
    activity: FragmentActivity,
    mainViewModel: MainViewModel = viewModel()
) {


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

        val localContextForToast = LocalContext.current

        Button(onClick = {

            mainViewModel.iniciarAutenticacion(activity) { resultado ->

                val mensaje = when (resultado) {
                    is com.example.keyguard.security.AuthResult.Exito -> "¡Autenticación Exitosa!"
                    is com.example.keyguard.security.AuthResult.Fallo -> "Autenticación fallida."
                    is com.example.keyguard.security.AuthResult.Error -> "Error: ${resultado.mensaje}"
                }
                Toast.makeText(localContextForToast, mensaje, Toast.LENGTH_SHORT).show()
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
