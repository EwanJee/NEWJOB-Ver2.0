package web.remember.configuration

import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.security.core.context.SecurityContextHolder
import java.util.concurrent.Executor

@EnableAsync
@Configuration
@AutoConfigureBefore(SecurityConfig::class)
class AsyncConfig : AsyncConfigurer {
    override fun getAsyncExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 5
        executor.maxPoolSize = 10
        executor.queueCapacity = 100
        executor.setThreadNamePrefix("AsyncExecutor-")

        // SecurityContext 전파를 위한 데코레이터
        executor.setTaskDecorator { runnable ->
            val context = SecurityContextHolder.getContext()
            Runnable {
                try {
                    SecurityContextHolder.setContext(context)
                    runnable.run()
                } finally {
                    SecurityContextHolder.clearContext()
                }
            }
        }

        executor.initialize()
        return executor
    }
}
