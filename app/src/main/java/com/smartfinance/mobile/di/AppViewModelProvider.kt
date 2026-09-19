package com.smartfinance.mobile.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.smartfinance.mobile.SmartFinanceApplication

/**
 * Factory global para inyectar los repositorios a los ViewModels.
 * 
 * Uso en Compose:
 * val viewModel: MiViewModel = viewModel(factory = AppViewModelProvider.Factory)
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Ejemplo de inicializador cuando crees tu primer ViewModel:
        /*
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SmartFinanceApplication)
            MiViewModel(
                authRepository = application.container.authRepository,
                vehicleRepository = application.container.vehicleRepository
            )
        }
        */
    }
}

/**
 * Extensión para acceder más fácil al contenedor de dependencias
 */
fun CreationExtras.smartFinanceApplication(): SmartFinanceApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SmartFinanceApplication)
