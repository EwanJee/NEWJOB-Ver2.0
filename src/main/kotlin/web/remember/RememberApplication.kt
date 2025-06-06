package web.remember

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class RememberApplication

fun main(args: Array<String>) {
    runApplication<RememberApplication>(*args)
}
