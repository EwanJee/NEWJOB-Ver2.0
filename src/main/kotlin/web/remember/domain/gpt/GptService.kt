package web.remember.domain.gpt

interface GptService {
    suspend fun getGptResponse(requestBody: String): String
}
