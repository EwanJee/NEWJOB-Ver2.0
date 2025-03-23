package web.remember.domain.error.annotation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import web.remember.domain.error.CustomException
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [CheckPhoneNumberValidator::class])
annotation class CheckPhoneNumber(
    val message: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

class CheckPhoneNumberValidator : ConstraintValidator<CheckPhoneNumber, String> {
    private lateinit var message: String

    override fun initialize(constraintAnnotation: CheckPhoneNumber) {
        message = constraintAnnotation.message
    }

    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext,
    ): Boolean {
        if (!isValidPhoneNumber(value ?: "")) {
            throw CustomException(message)
        }
        return true
    }

    private fun isValidPhoneNumber(value: String): Boolean {
        val regex = "^(010-?)?\\d{3,4}-?\\d{4}$"
        return value.matches(Regex(regex))
    }
}
