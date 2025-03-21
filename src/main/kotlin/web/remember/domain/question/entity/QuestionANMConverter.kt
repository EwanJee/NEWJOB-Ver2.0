package web.remember.domain.question.entity

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class QuestionANMConverter : AttributeConverter<List<QuestionANM>, String> {
    override fun convertToDatabaseColumn(attribute: List<QuestionANM>?): String? {
        // List<QuestionANM> -> String (예: "MOVE_ON,ABILITY")
        return attribute?.joinToString(",") { it.name }
    }

    override fun convertToEntityAttribute(dbData: String?): List<QuestionANM>? {
        // String -> List<QuestionANM>
        return dbData
            ?.split(",")
            ?.map { name -> QuestionANM.valueOf(name) }
    }
}
