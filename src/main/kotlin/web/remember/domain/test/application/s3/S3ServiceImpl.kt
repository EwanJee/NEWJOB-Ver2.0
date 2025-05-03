package web.remember.domain.test.application.s3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import web.remember.domain.error.CustomException
import java.net.URL

@Service
class S3ServiceImpl(
    private val s3Client: AmazonS3,
) : S3Service {
    @Value("\${cloud.aws.s3.bucket}")
    private lateinit var bucketName: String

    override fun uploadFile(
        file: ByteArray,
        fileName: String,
    ): String {
        val metadata =
            ObjectMetadata().apply {
                contentLength = file.size.toLong()
                contentType = "application/pdf"
            }
        try {
            s3Client.putObject(bucketName, fileName, file.inputStream(), metadata)
        } catch (e: Exception) {
            throw CustomException("파일 업로드에 실패했습니다. ${e.message}")
        }
        return getFileUrl(fileName)
    }

    override fun getFileUrl(fileName: String): String {
        val url: URL =
            try {
                s3Client.getUrl(bucketName, fileName)
            } catch (e: Exception) {
                throw CustomException("파일을 찾을 수 없습니다. ${e.message}")
            }
//        val encodedUrl = URLEncoder.encode(url.toString(), "UTF-8")
        return url.toString()
    }
}
