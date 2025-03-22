package web.remember.domain.test.repository

interface RedisHashOperations {
    fun scanKeys(pattern: String): Set<String>
}
