package web.remember.aop

import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Component
@Aspect
class HttpLogAspect {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    fun restControllerMethods() {
    }

    @Around("restControllerMethods()")
    fun logAround(joinPoint: ProceedingJoinPoint): Any? {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val request: HttpServletRequest? = requestAttributes?.request

        val startTime = System.currentTimeMillis()
        val result =
            try {
                joinPoint.proceed() // 실제 컨트롤러 메서드 실행
            } catch (ex: Throwable) {
                val duration = System.currentTimeMillis() - startTime
                logger.error("[HTTP] Exception in ${joinPoint.signature}: ${ex.message} (${duration}ms)", ex)
                throw ex
            }
        val duration = System.currentTimeMillis() - startTime

        if (request != null) {
            val uri = request.requestURI
            val method = request.method
            val queryString = request.queryString?.let { "?$it" } ?: ""
            val handlerMethod = joinPoint.signature.declaringType.simpleName
            val params = joinPoint.args.joinToString(", ") { it.toString() }

            logger.info("[HTTP] {} {}{} -> {}() | {}ms", method, uri, queryString, handlerMethod, duration)
        } else {
            logger.info("[HTTP] Executed {} in {}ms", joinPoint.signature, duration)
        }

        return result
    }

    @Around("@annotation(MeasureExecutionTime)")
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val startTime = System.currentTimeMillis()
        val proceed = joinPoint.proceed()
        val endTime = System.currentTimeMillis()
        logger.info("[METHOD] {} executed in {} ms", joinPoint.signature, (endTime - startTime))
        return proceed
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class MeasureExecutionTime
