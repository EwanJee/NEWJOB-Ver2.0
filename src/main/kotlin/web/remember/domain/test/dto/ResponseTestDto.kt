package web.remember.domain.test.dto

import java.time.LocalDateTime

data class ResponseTestDto(
    val id: Long,
    val name: String,
    val url: String,
    val paid: Boolean,
    val createdAt: LocalDateTime,
)
