@file:Suppress("ktlint:standard:no-wildcard-imports")

package web.remember.domain.test.entity

import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.*
import web.remember.domain.question.entity.TestType

@Table(name = "test")
@Entity
class Test(
    memberId: String,
    testType: TestType,
    data: MutableMap<String, String>?,
) {
    constructor(memberId: String, testType: TestType) : this(memberId, testType, null)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null

    @Column(name = "member_id")
    val memberId: String = memberId

    @Column(name = "test_type")
    val testType: TestType = testType

    @Column(name = "data", columnDefinition = "jsonb")
    @org.hibernate.annotations.Type(JsonType::class)
    val data: MutableMap<String, String>? = data
}
