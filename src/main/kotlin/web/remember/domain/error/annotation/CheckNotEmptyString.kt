package web.remember.domain.error.annotation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import web.remember.domain.error.CustomException
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [NotEmptyStringValidator::class])
annotation class CheckNotEmptyString(
    val message: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

class NotEmptyStringValidator : ConstraintValidator<CheckNotEmptyString, String> {
    private lateinit var message: String

    override fun initialize(constraintAnnotation: CheckNotEmptyString) {
        message = constraintAnnotation.message
    }

    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext,
    ): Boolean {
        if (value.isNullOrBlank()) {
            throw CustomException(message)
        }
        return true
    }
}
