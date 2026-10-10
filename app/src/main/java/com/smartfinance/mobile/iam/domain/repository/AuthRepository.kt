package com.smartfinance.mobile.iam.domain.repository

import com.smartfinance.mobile.iam.domain.model.User

/**
 * Contrato de autenticación independiente de Retrofit y de la interfaz gráfica.
 */
interface AuthRepository {
    suspend fun signIn(username: String, password: String): Result<User>
    suspend fun signUp(username: String, email: String, password: String): Result<User>
    suspend fun refreshToken(): Result<User>
    suspend fun signOut()
    fun getCurrentUser(): User?
}
