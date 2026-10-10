package com.smartfinance.mobile.core.network

import com.google.gson.Gson
import com.smartfinance.mobile.core.storage.TokenStorage
import com.smartfinance.mobile.iam.infrastructure.remote.AuthenticatedUserResource
import com.smartfinance.mobile.iam.infrastructure.remote.RefreshTokenResource
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route

/**
 * Maneja respuestas 401 Unauthorized renovando el token de acceso mediante el refresh token.
 * Usa un mutex (synchronized) para evitar peticiones concurrentes de refresh token,
 * cumpliendo con la directiva del backend de no reintentar indefinidamente.
 */
class TokenAuthenticator(
    private val tokenStorage: TokenStorage
) : Authenticator {

    private val gson = Gson()
    private val lock = Any()

    override fun authenticate(route: Route?, response: Response): Request? {
        // Evitar bucles infinitos si la petición ya fue reintentada más de una vez
        if (responseCount(response) >= 2) {
            tokenStorage.clearSession()
            return null
        }

        val failedPath = response.request.url.encodedPath
        if (failedPath.contains("/auth/")) {
            return null
        }

        synchronized(lock) {
            val currentToken = tokenStorage.getToken()
            val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")?.trim()

            // Si otro hilo ya renovó el token mientras esperábamos el lock, reintentar con el nuevo token
            if (!currentToken.isNullOrBlank() && currentToken != requestToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            val refreshToken = tokenStorage.getRefreshToken()
            if (refreshToken.isNullOrBlank()) {
                tokenStorage.clearSession()
                return null
            }

            // Realizar llamada síncrona directa a POST /api/v1/auth/tokens sin interceptores circulares
            val newTokens = performTokenRefresh(refreshToken)
            return if (newTokens != null) {
                tokenStorage.saveSession(
                    token = newTokens.token,
                    refreshToken = newTokens.refreshToken,
                    userId = newTokens.id,
                    username = newTokens.username,
                    roles = newTokens.roles
                )
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${newTokens.token}")
                    .build()
            } else {
                tokenStorage.clearSession()
                null
            }
        }
    }

    private fun performTokenRefresh(refreshToken: String): AuthenticatedUserResource? {
        return try {
            val refreshClient = OkHttpClient.Builder().build()
            val jsonPayload = gson.toJson(RefreshTokenResource(refreshToken))
            val body = jsonPayload.toRequestBody("application/json; charset=utf-8".toMediaType())

            val refreshUrl = ApiConfig.BASE_URL.trimEnd('/') + "/api/v1/auth/tokens"
            val refreshRequest = Request.Builder()
                .url(refreshUrl)
                .post(body)
                .build()

            refreshClient.newCall(refreshRequest).execute().use { refreshResponse ->
                if (refreshResponse.isSuccessful) {
                    val responseBody = refreshResponse.body?.string()
                    if (!responseBody.isNullOrBlank()) {
                        gson.fromJson(responseBody, AuthenticatedUserResource::class.java)
                    } else null
                } else {
                    null
                }
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var prior = response.priorResponse
        while (prior != null) {
            result++
            prior = prior.priorResponse
        }
        return result
    }
}
