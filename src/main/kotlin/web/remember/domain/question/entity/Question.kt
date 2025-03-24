@file:Suppress("ktlint:standard:no-wildcard-imports")

package web.remember.domain.question.entity

import jakarta.persistence.*
import web.remember.domain.question.entity.TestType.CAREER
import web.remember.domain.question.entity.TestType.RETIREMENT
import java.util.*

@Table(name = "question")
@Entity
class Question(
    group: QuestionGroup,
    score: Int,
    anm: MutableList<QuestionANM>?,
    content: String,
) {
    @Id
    @Column(name = "id")
    val id: String = UUID.randomUUID().toString()

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20)
    val type: TestType = QuestionGroup.convertToType(group)

    @Enumerated(EnumType.STRING)
    @Column(name = "q_group", length = 20)
    val group: QuestionGroup = group

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 20)
    val ctype: QuestionCtype? = QuestionGroup.convertToCtype(group)

    @Column(name = "score")
    val score: Int = score

    @Convert(converter = QuestionANMConverter::class)
    @Column(name = "anms", columnDefinition = "TEXT")
    val anms: MutableList<QuestionANM>? = anm

    @Column(name = "content")
    val content: String = content
}

enum class TestType {
    CAREER,
    RETIREMENT,
}

@Suppress("ktlint:standard:enum-entry-name-case")
enum class QuestionGroup {
    CAREER_WHAT,
    CAREER_HOW,
    CAREER_WHY,
    CAREER_성과평가,
    CAREER_네트워크,
    CAREER_팀웍동료,
    CAREER_조직,
    CAREER_업계,
    CAREER_전문분야,
    CAREER_영역확장,
    CAREER_영역개발,
    CAREER_업무재조성,
    CAREER_지원개발,
    CAREER_학습계획,
    CAREER_실천,
    CAREER_월드지수,
    RETIREMENT_1,
    RETIREMENT_2,
    RETIREMENT_3,
    RETIREMENT_4,
    RETIREMENT_5,
    RETIREMENT_6,
    ;

    companion object {
        fun convertToKorean(group: String): QuestionGroup? =
            when (group) {
                "What" -> CAREER_WHAT
                "How" -> CAREER_HOW
                "Why" -> CAREER_WHY
                "Performance_Evaluation" -> CAREER_성과평가
                "Network" -> CAREER_네트워크
                "Teamwork" -> CAREER_팀웍동료
                "Organization" -> CAREER_조직
                "Industry" -> CAREER_업계
                "Specialty" -> CAREER_전문분야
                "Area_Expansion" -> CAREER_영역확장
                "Area_Development" -> CAREER_영역개발
                "Business_Restructuring" -> CAREER_업무재조성
                "Support_Development" -> CAREER_지원개발
                "Learning_Plan" -> CAREER_학습계획
                "Practice" -> CAREER_실천
                "World_Index" -> CAREER_월드지수
                "Retirement_1" -> RETIREMENT_1
                "Retirement_2" -> RETIREMENT_2
                "Retirement_3" -> RETIREMENT_3
                "Retirement_4" -> RETIREMENT_4
                "Retirement_5" -> RETIREMENT_5
                "Retirement_6" -> RETIREMENT_6
                else -> null
            }

        fun convertToEnglish(group: QuestionGroup): String =
            when (group) {
                CAREER_WHAT -> "What"
                CAREER_HOW -> "How"
                CAREER_WHY -> "Why"
                CAREER_성과평가 -> "Performance_Evaluation"
                CAREER_네트워크 -> "Network"
                CAREER_팀웍동료 -> "Teamwork"
                CAREER_조직 -> "Organization"
                CAREER_업계 -> "Industry"
                CAREER_전문분야 -> "Specialty"
                CAREER_영역확장 -> "Area_Expansion"
                CAREER_영역개발 -> "Area_Development"
                CAREER_업무재조성 -> "Business_Restructuring"
                CAREER_지원개발 -> "Support_Development"
                CAREER_학습계획 -> "Learning_Plan"
                CAREER_실천 -> "Practice"
                CAREER_월드지수 -> "World_Index"
                RETIREMENT_1 -> "Retirement_1"
                RETIREMENT_2 -> "Retirement_2"
                RETIREMENT_3 -> "Retirement_3"
                RETIREMENT_4 -> "Retirement_4"
                RETIREMENT_5 -> "Retirement_5"
                RETIREMENT_6 -> "Retirement_6"
            }

        fun convertToType(group: QuestionGroup): TestType {
            if (group.name.startsWith("CAREER")) {
                return CAREER
            }
            return RETIREMENT
        }

        fun convertToCtype(group: QuestionGroup): QuestionCtype? =
            when (group) {
                CAREER_WHAT -> QuestionCtype.CONTROL
                CAREER_HOW -> QuestionCtype.CONNECTION
                CAREER_WHY -> QuestionCtype.CONDITION

                CAREER_성과평가 -> QuestionCtype.CONNECTION
                CAREER_네트워크 -> QuestionCtype.CHARACTER
                CAREER_팀웍동료 -> QuestionCtype.CONTROL

                CAREER_조직 -> QuestionCtype.CONNECTION
                CAREER_업계 -> QuestionCtype.CHARACTER
                CAREER_전문분야 -> QuestionCtype.CHALLENGE

                CAREER_영역확장 -> QuestionCtype.CHARACTER
                CAREER_영역개발 -> QuestionCtype.CONTROL
                CAREER_업무재조성 -> QuestionCtype.CONDITION

                CAREER_지원개발 -> QuestionCtype.CONDITION
                CAREER_학습계획 -> QuestionCtype.CHARACTER
                CAREER_실천 -> QuestionCtype.CHALLENGE

                CAREER_월드지수 -> QuestionCtype.WORLD
                else -> null
            }
    }
}

enum class QuestionCtype {
    // ONLY IF CAREER
    CHARACTER,
    CONNECTION,
    CONDITION,
    CHALLENGE,
    CONTROL,
    WORLD,
}

enum class QuestionANM {
    ABILITY,
    NETWORK_POWER,
    MOVE_ON,
}
