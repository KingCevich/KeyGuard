package com.example.keyguard.security

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity


sealed class AuthResult {
    object Exito : AuthResult()
    object Fallo : AuthResult()
    data class Error(val mensaje: String) : AuthResult()
}

class Biometrica(private val activity: FragmentActivity) {

    fun autenticar(

        onResultado: (AuthResult) -> Unit
    ) {

        val biometricManager = BiometricManager.from(activity)
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL

        when (biometricManager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                onResultado(AuthResult.Error("Este dispositivo no tiene sensor de huella."))
                return
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                onResultado(AuthResult.Error("El sensor de huella no está disponible ahora."))
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                onResultado(AuthResult.Error("No tienes una huella registrada."))
                return
            }

        }


        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onResultado(AuthResult.Exito)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onResultado(AuthResult.Fallo)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode != BiometricPrompt.ERROR_USER_CANCELED && errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    onResultado(AuthResult.Error("Error de autenticación: $errString"))
                }

            }
        }

        val biometricPrompt = BiometricPrompt(activity, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Desbloquear KeyGuard")
            .setSubtitle("Usa tu huella para continuar")
            .setNegativeButtonText("Cancelar")
            .build()


        biometricPrompt.authenticate(promptInfo)
    }
}
