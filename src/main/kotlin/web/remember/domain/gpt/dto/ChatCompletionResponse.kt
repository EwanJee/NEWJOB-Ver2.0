import com.fasterxml.jackson.annotation.JsonProperty

data class ChatCompletionResponse(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("object")
    val `object`: String,
    @JsonProperty("created")
    val created: Long,
    @JsonProperty("model")
    val model: String,
    @JsonProperty("choices")
    val choices: List<Choice>,
    @JsonProperty("usage")
    val usage: Usage,
    @JsonProperty("service_tier")
    val serviceTier: String,
    @JsonProperty("system_fingerprint")
    val systemFingerprint: String,
) {
    data class Choice(
        @JsonProperty("index")
        val index: Int,
        @JsonProperty("message")
        val message: Message,
        // 필요 시 `finish_reason`도 여기에 추가 가능
    )

    data class Message(
        @JsonProperty("role")
        val role: String,
        @JsonProperty("content")
        val content: String,
    )

    data class Usage(
        @JsonProperty("prompt_tokens")
        val promptTokens: Int,
        @JsonProperty("completion_tokens")
        val completionTokens: Int,
        @JsonProperty("total_tokens")
        val totalTokens: Int,
        @JsonProperty("prompt_tokens_details")
        val promptTokensDetails: TokenDetails,
        @JsonProperty("completion_tokens_details")
        val completionTokensDetails: TokenDetails,
    )

    data class TokenDetails(
        @JsonProperty("cached_tokens")
        val cachedTokens: Int? = null,
        @JsonProperty("audio_tokens")
        val audioTokens: Int? = null,
        @JsonProperty("reasoning_tokens")
        val reasoningTokens: Int? = null,
        @JsonProperty("accepted_prediction_tokens")
        val acceptedPredictionTokens: Int? = null,
        @JsonProperty("rejected_prediction_tokens")
        val rejectedPredictionTokens: Int? = null,
    )
}
