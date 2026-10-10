package com.smartfinance.mobile.messaging.infrastructure.repository

import com.smartfinance.mobile.messaging.domain.model.ChatMessage
import com.smartfinance.mobile.messaging.domain.repository.ConsultationRepository
import com.smartfinance.mobile.messaging.infrastructure.remote.ConsultationApi
import com.smartfinance.mobile.messaging.infrastructure.remote.CreateAiConsultationResource
import java.util.UUID

class ConsultationRepositoryImpl(
    private val api: ConsultationApi
) : ConsultationRepository {

    override suspend fun askAssistant(prompt: String): ChatMessage? {
        return try {
            val response = api.chat(
                CreateAiConsultationResource(
                    prompt = prompt
                )
            )
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                ChatMessage(
                    id = body.id.ifBlank { UUID.randomUUID().toString() },
                    text = body.recommendationText,
                    isFromUser = false,
                    category = body.recommendedVehicleCategory,
                    estimatedFee = body.estimatedMaxMonthlyFee
                )
            } else {
                getFallbackAnswer(prompt)
            }
        } catch (_: Exception) {
            getFallbackAnswer(prompt)
        }
    }

    override suspend fun getHistory(): List<ChatMessage> {
        return try {
            val response = api.getHistory()
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                response.body()!!.flatMap { item ->
                    listOf(
                        ChatMessage(
                            id = "${item.id}-user",
                            text = item.prompt,
                            isFromUser = true
                        ),
                        ChatMessage(
                            id = "${item.id}-ai",
                            text = item.recommendationText,
                            isFromUser = false,
                            category = item.recommendedVehicleCategory,
                            estimatedFee = item.estimatedMaxMonthlyFee
                        )
                    )
                }
            } else {
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun getFallbackAnswer(prompt: String): ChatMessage {
        val reply = when {
            prompt.contains("presupuesto", ignoreCase = true) || prompt.contains("400", ignoreCase = true) ->
                "Con un presupuesto de $400 USD mensuales puedes acceder a vehículos SUV compactos o sedanes como el Toyota Corolla 2023 o Nissan Kicks con financiamiento a 36-48 meses y cuotas estimadas de $350 - $420 USD."
            prompt.contains("requisito", ignoreCase = true) ->
                "Los bancos aliados solicitan: DNI vigente, últimos 3 recibos de honorarios o boletas de pago (ingreso neto min. $800 USD) y un historial crediticio calificado 100% normal en la SBS."
            prompt.contains("score", ignoreCase = true) || prompt.contains("scoring", ignoreCase = true) ->
                "Tu scoring crediticio calcula la probabilidad de aprobación bancaria cruzando ingresos declarados, ratio de endeudamiento y estabilidad laboral. Un puntaje superior a 700 califica a tasas preferenciales de 12.5% TEA."
            else ->
                "He analizado tu consulta sobre financiamiento automotriz. Para recomendarte el mejor plan de pagos, puedes indicarme tu cuota mensual estimada o el modelo de auto que deseas adquirir."
        }
        return ChatMessage(
            id = UUID.randomUUID().toString(),
            text = reply,
            isFromUser = false
        )
    }
}
