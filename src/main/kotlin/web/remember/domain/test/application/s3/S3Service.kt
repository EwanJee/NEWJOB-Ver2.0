package web.remember.domain.test.application.s3

interface S3Service {
    fun uploadFile(
        file: ByteArray,
        fileName: String,
    ): String

    fun getFileUrl(fileName: String): String
}
