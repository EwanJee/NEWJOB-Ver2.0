package web.remember.domain.test

import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.*
import web.remember.domain.question.Type

@Table(name = "test")
@Entity
class Test(
    memberId: String,
    testType: Type,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null

    @Column(name = "member_id")
    val memberId: String = memberId

    @Column(name = "test_type")
    val testType: Type = testType

    @Column(name = "test_type")
    @org.hibernate.annotations.Type(JsonType::class)
    val data: Map<String, String> = mutableMapOf()
}
