package web.remember.domain.gpt

interface GptService {
    fun getGptResponse(requestBody: String): String
}
