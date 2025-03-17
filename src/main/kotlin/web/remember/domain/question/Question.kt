package web.remember.domain.question

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import web.remember.domain.question.Type.CAREER
import web.remember.domain.question.Type.RETIREMENT
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
    val type: String = QuestionGroup.convertToType(group).toString()

    @Column(name = "group", length = 10)
    val group: QuestionGroup = group

    @Column(name = "category", length = 20)
    val category: String? = QuestionGroup.convertToCategory(group).toString()

    @Column(name = "score")
    val score: Int = score

    @Column(name = "anm", length = 20)
    val anm: String? = anm.toString()
}

enum class Type {
    CAREER,
    RETIREMENT,
}

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
        fun convertToType(group: QuestionGroup): Type {
            if (group.name.startsWith("CAREER")) {
                return CAREER
            }
            return RETIREMENT
        }

        fun convertToCategory(group: QuestionGroup): QuestionCTYPE? =
            when (group) {
                CAREER_WHAT -> QuestionCTYPE.CONTROL
                CAREER_HOW -> QuestionCTYPE.CONNECTION
                CAREER_WHY -> QuestionCTYPE.CONDITION

                CAREER_성과평가 -> QuestionCTYPE.CONNECTION
                CAREER_네트워크 -> QuestionCTYPE.CHARACTER
                CAREER_팀웍동료 -> QuestionCTYPE.CONTROL

                CAREER_조직 -> QuestionCTYPE.CONNECTION
                CAREER_업계 -> QuestionCTYPE.CHARACTER
                CAREER_전문분야 -> QuestionCTYPE.CHALLENGE

                CAREER_영역확장 -> QuestionCTYPE.CHARACTER
                CAREER_영역개발 -> QuestionCTYPE.CONTROL
                CAREER_업무재조성 -> QuestionCTYPE.CONDITION

                CAREER_지원개발 -> QuestionCTYPE.CONDITION
                CAREER_학습계획 -> QuestionCTYPE.CHARACTER
                CAREER_실천 -> QuestionCTYPE.CHALLENGE

                CAREER_월드지수 -> QuestionCTYPE.WORLD
                else -> null
            }
    }
}

enum class QuestionCTYPE {
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
