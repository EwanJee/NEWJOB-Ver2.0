package com.nasa.todaynasa.application.service.apod.request.gpt

import Message
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestAnswerGpt(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("object")
    val `object`: String,
    @JsonProperty("created")
    val created: String,
    @JsonProperty("model")
    val model: String,
    @JsonProperty("choices")
    val choices: List<Choice>,
    @JsonProperty("usage")
    val usage: Usage,
    @JsonProperty("system_fingerprint")
    val system_fingerprint: String,
)

data class Choice(
    @JsonProperty("index")
    val index: Int,
    @JsonProperty("message")
    val message: Message,
    @JsonProperty("logprobs")
    val logprobs: Any?,
    @JsonProperty("finish_reason")
    val finish_reason: String,
)

data class Usage(
    @JsonProperty("prompt_tokens")
    val prompt_tokens: Int,
    @JsonProperty("completion_tokens")
    val completion_tokens: Int,
    @JsonProperty("total_tokens")
    val total_tokens: Int,
)
