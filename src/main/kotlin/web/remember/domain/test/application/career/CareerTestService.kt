package web.remember.domain.test.application.career

import web.remember.domain.test.presentation.career.dto.RequestScoreUpdateDto

interface CareerTestService {
    fun startTest(
        questionMap: Map<String, Map<String, String>>,
        memberId: String,
    ): String

    fun updateScore(dto: RequestScoreUpdateDto)

    fun finishTest(
        memberId: String,
        testId: String,
    )

    fun updateCareerAge(
        testId: String,
        memberId: String,
        age: String,
    )

    fun findDataById(testId: String): MutableMap<String, String>
}
