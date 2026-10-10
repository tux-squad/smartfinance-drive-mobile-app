package com.smartfinance.mobile.iam.infrastructure.repository

import com.smartfinance.mobile.core.storage.TokenStorage
import com.smartfinance.mobile.iam.domain.model.User
import com.smartfinance.mobile.iam.domain.repository.AuthRepository
import com.smartfinance.mobile.iam.infrastructure.remote.AuthApi
import com.smartfinance.mobile.iam.infrastructure.remote.RefreshTokenResource
import com.smartfinance.mobile.iam.infrastructure.remote.SignInResource
import com.smartfinance.mobile.iam.infrastructure.remote.SignUpResource

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun signIn(username: String, password: String): Result<User> {
        return try {
            val request = SignInResource(username.trim(), password)
            val response = api.signIn(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    tokenStorage.saveSession(
                        token = body.token,
                        refreshToken = body.refreshToken,
                        userId = body.id,
                        username = body.username,
                        roles = body.roles
                    )

                    val user = User(
                        id = body.id,
                        username = body.username,
                        email = body.username,
                        roles = body.roles.ifEmpty { listOf("ROLE_USER") },
                        accessToken = body.token,
                        refreshToken = body.refreshToken
                    )
                    Result.success(user)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.failure(Exception("Error en inicio de sesión (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Servicio de autenticación no disponible", e))
        }
    }

    override suspend fun signUp(username: String, email: String, password: String): Result<User> {
        return try {
            val request = SignUpResource(username = email.trim(), password = password)
            val response = api.signUp(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val user = User(
                        id = body.id,
                        username = body.username,
                        email = body.username,
                        roles = body.roles.ifEmpty { listOf("ROLE_USER") }
                    )
                    Result.success(user)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.failure(Exception("Error en registro (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Servicio de registro no disponible", e))
        }
    }

    override suspend fun refreshToken(): Result<User> {
        val currentRefreshToken = tokenStorage.getRefreshToken()
        if (currentRefreshToken.isNullOrBlank()) {
            tokenStorage.clearSession()
            return Result.failure(Exception("No existe refresh token almacenado"))
        }

        return try {
            val response = api.refreshToken(RefreshTokenResource(currentRefreshToken))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    tokenStorage.saveSession(
                        token = body.token,
                        refreshToken = body.refreshToken,
                        userId = body.id,
                        username = body.username,
                        roles = body.roles
                    )
                    val user = User(
                        id = body.id,
                        username = body.username,
                        email = body.username,
                        roles = body.roles.ifEmpty { listOf("ROLE_USER") },
                        accessToken = body.token,
                        refreshToken = body.refreshToken
                    )
                    Result.success(user)
                } else {
                    Result.failure(Exception("Respuesta vacía al renovar token"))
                }
            } else {
                tokenStorage.clearSession()
                Result.failure(Exception("Renovación de token expirada o inválida (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error al contactar servicio de renovación", e))
        }
    }

    override suspend fun signOut() {
        try {
            api.revokeCurrentSession()
        } catch (_: Exception) {
            // Se ignora el fallo de red para garantizar la limpieza local
        } finally {
            tokenStorage.clearSession()
        }
    }

    override fun getCurrentUser(): User? {
        if (!tokenStorage.hasSession()) return null
        return User(
            id = tokenStorage.getUserId().orEmpty(),
            username = tokenStorage.getUsername().orEmpty(),
            email = tokenStorage.getUsername().orEmpty(),
            roles = tokenStorage.getRoles().ifEmpty { listOf("ROLE_USER") },
            accessToken = tokenStorage.getToken().orEmpty(),
            refreshToken = tokenStorage.getRefreshToken().orEmpty()
        )
    }
}
