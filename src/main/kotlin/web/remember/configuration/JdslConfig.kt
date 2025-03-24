package web.remember.configuration

import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JdslConfig {
    @Bean
    fun context(): JpqlRenderContext = JpqlRenderContext()

    @Bean
    fun renderer(): JpqlRenderer = JpqlRenderer()
}
