package web.remember

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RememberApplication

fun main(args: Array<String>) {
    runApplication<RememberApplication>(*args)
}
