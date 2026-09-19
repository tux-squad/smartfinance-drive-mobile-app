package com.smartfinance.mobile.iam.infrastructure.repository

import com.smartfinance.mobile.core.storage.TokenStorage
import com.smartfinance.mobile.iam.domain.model.User
import com.smartfinance.mobile.iam.domain.repository.AuthRepository
import com.smartfinance.mobile.iam.infrastructure.remote.AuthApi
import com.smartfinance.mobile.iam.infrastructure.remote.SignInResource
import com.smartfinance.mobile.iam.infrastructure.remote.SignUpResource

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun signIn(username: String, password: String): Result<User> {
        return try {
            val request = SignInResource(username, password)
            val response = api.signIn(request)
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    tokenStorage.saveToken(body.token)
                    tokenStorage.saveUserId(body.id.toString())
                    
                    val user = User(
                        id = body.id.toString(),
                        username = body.username,
                        email = body.username, // API uses username as email
                        role = body.roles.firstOrNull() ?: "ROLE_USER",
                        accessToken = body.token,
                        refreshToken = body.refreshToken
                    )
                    Result.success(user)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Result.failure(Exception("HTTP Error: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
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
                        id = body.id.toString(),
                        username = body.username,
                        email = body.username,
                        role = body.roles.firstOrNull() ?: "ROLE_USER"
                    )
                    Result.success(user)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Result.failure(Exception("HTTP Error: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        tokenStorage.clearToken()
    }
}
