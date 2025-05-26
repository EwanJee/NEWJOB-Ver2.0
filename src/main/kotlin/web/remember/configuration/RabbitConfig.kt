package web.remember.configuration

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry

@EnableRetry
@Configuration
class RabbitConfig {
    @Bean
    fun queue(): Queue {
        val args =
            mapOf(
                "x-dead-letter-exchange" to "pdf.dlx", // DLX 이름
                "x-dead-letter-routing-key" to "pdf.dlq", // DLQ의 라우팅 키
            )
        return Queue("pdf.queue", true, false, false, args)
    }

    @Bean
    fun exchange(): DirectExchange = DirectExchange("pdf.exchange")

    @Bean
    fun binding(
        queue: Queue,
        exchange: DirectExchange,
    ): Binding = BindingBuilder.bind(queue).to(exchange).with("pdf.create")

    @Bean
    fun deadLetterExchange(): DirectExchange = DirectExchange("pdf.dlx")

    @Bean
    fun deadLetterQueue(): Queue = Queue("pdf.dlq")

    @Bean
    fun deadLetterBinding(): Binding =
        BindingBuilder
            .bind(deadLetterQueue())
            .to(deadLetterExchange())
            .with("pdf.dlq")
}
