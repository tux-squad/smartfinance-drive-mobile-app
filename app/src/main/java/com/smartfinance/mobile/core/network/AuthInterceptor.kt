package com.smartfinance.mobile.core.network

import com.smartfinance.mobile.core.storage.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Agrega el token de acceso a las peticiones protegidas.
 *
 * Las rutas de inicio de sesion y registro se dejan publicas.
 */
class AuthInterceptor(private val tokenStorage: TokenStorage) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val path = originalRequest.url.encodedPath

        if (path.contains("/auth/sessions") || path.contains("/auth/registrations")) {
            return chain.proceed(originalRequest)
        }

        val token = tokenStorage.getToken()
        val request = originalRequest.newBuilder().apply {
            if (!token.isNullOrBlank()) {
                header("Authorization", "Bearer " + token)
            }
        }.build()

        return chain.proceed(request)
    }
}
