package com.example.keyguard.security

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity


class Biometrica(private val context: Context) {


    private val promptInfo: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Autenticación Biométrica")
        .setSubtitle("Inicia sesión usando tu huella o rostro")
        .setNegativeButtonText("Cancelar")
        .build()

    private fun setupBiometricPrompt(
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onError: (Int, CharSequence) -> Unit,
        onFailure: () -> Unit
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(context)
        val activity = context as? FragmentActivity
            ?: throw IllegalStateException("El contexto debe ser una FragmentActivity para usar BiometricPrompt")

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess(result)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError(errorCode, errString)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onFailure()
            }
        }
        return BiometricPrompt(activity, executor, callback)
    }


    fun authenticate(
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onError: (Int, CharSequence) -> Unit = { _, _ -> },
        onFailure: () -> Unit = {}
    ) {
        val biometricManager = BiometricManager.from(context)

        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {

            val biometricPrompt = setupBiometricPrompt(onSuccess, onError, onFailure)
            biometricPrompt.authenticate(promptInfo)
        } else {

            Toast.makeText(context, "No se puede autenticar con biometría en este dispositivo.", Toast.LENGTH_LONG).show()
        }
    }
}
