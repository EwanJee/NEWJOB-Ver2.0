package web.remember.domain.error

class CustomException(
    override val message: String,
) : Exception(message)
