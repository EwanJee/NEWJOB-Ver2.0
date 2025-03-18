@file:Suppress("ktlint:standard:no-wildcard-imports")

package web.remember.domain.question.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import web.remember.domain.question.entity.TestType.CAREER
import web.remember.domain.question.entity.TestType.RETIREMENT
import java.util.*

@Table(name = "question")
@Entity
class Question(
    group: QuestionGroup,
    score: Int,
    anm: QuestionANM?,
) {
    @Id
    @Column(name = "id")
    val id: String = UUID.randomUUID().toString()

    @Column(name = "type")
    val type: TestType = QuestionGroup.convertToType(group)

    @Column(name = "group")
    val group: QuestionGroup = group

    @Column(name = "category", length = 20)
    val ctype: QuestionCtype? = QuestionGroup.convertToCtype(group)

    @Column(name = "score")
    val score: Int = score

    @Column(name = "anm", length = 20)
    val anm: QuestionANM? = anm
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
