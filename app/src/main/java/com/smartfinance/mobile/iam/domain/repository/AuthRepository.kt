package com.smartfinance.mobile.iam.domain.repository

import com.smartfinance.mobile.iam.domain.model.User

/**
 * Contrato de autenticacion independiente de Retrofit y de la interfaz grafica.
 */
interface AuthRepository {
    suspend fun signIn(username: String, password: String): Result<User>
    suspend fun signUp(username: String, email: String, password: String): Result<User>
    suspend fun signOut()
}
