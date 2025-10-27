package com.example.keyguard.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.keyguard.data.model.contrasena

class ContrasenaStoreViewModel : ViewModel() {
    val items = mutableStateListOf<contrasena>()
    fun add(item: contrasena) {
        items.add(item.copy(id = if (item.id == 0) (items.size + 1) else item.id))
    }
}
