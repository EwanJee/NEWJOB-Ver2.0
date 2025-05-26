package web.remember.domain.test.stream

import org.springframework.security.core.context.SecurityContext
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

interface TestStreamService {
    fun emit(
        emitter: SseEmitter,
        pdfId: String,
        context: SecurityContext,
    )
}
