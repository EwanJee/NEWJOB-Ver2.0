package web.remember.domain.test.repository

import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Component

@Component
class RedisHashOperationsImpl(
    private val redisTemplate: RedisTemplate<String, String>,
) : RedisHashOperations {
    override fun scanKeys(pattern: String): Set<String> {
        val options =
            ScanOptions
                .scanOptions()
                .match(pattern)
                .count(1000)
                .build()
        val connection =
            redisTemplate.connectionFactory?.connection
                ?: return emptySet()
        val result = mutableSetOf<String>()

        connection.use {
            val cursor: Cursor<ByteArray> = it.keyCommands().scan(options)
            cursor.forEachRemaining { item ->
                result.add(String(item))
            }
        }
        return result
    }
}
