@file:Suppress("ktlint:standard:no-wildcard-imports")

package web.remember.domain.test.entity

import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.*
import org.hibernate.annotations.DynamicUpdate
import web.remember.domain.question.entity.TestType

@DynamicUpdate // 변경된 필드만 업데이트
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

    @Enumerated(EnumType.STRING)
    @Column(name = "test_type")
    val testType: TestType = testType

    @Column(name = "data", columnDefinition = "jsonb")
    @org.hibernate.annotations.Type(JsonType::class)
    var data: MutableMap<String, String>? = data

    @Column(name = "url")
    var url: String = ""
}
