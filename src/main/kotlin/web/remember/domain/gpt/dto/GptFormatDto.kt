import com.fasterxml.jackson.annotation.JsonProperty

data class GptFormatDto(
    @JsonProperty("model")
    val model: String,
    @JsonProperty("messages")
    val messages: MutableList<Message>,
    @JsonProperty("response_format")
    val responseFormat: ResponseFormat,
    @JsonProperty("temperature")
    val temperature: Double,
    @JsonProperty("max_completion_tokens")
    val maxCompletionTokens: Int,
    @JsonProperty("top_p")
    val topP: Double,
    @JsonProperty("frequency_penalty")
    val frequencyPenalty: Int,
    @JsonProperty("presence_penalty")
    val presencePenalty: Int,
    @JsonProperty("store")
    val store: Boolean,
) {
    data class Message(
        @JsonProperty("role")
        val role: String,
        @JsonProperty("content")
        val content: Content,
    )

    data class Content(
        @JsonProperty("type")
        val type: String,
        @JsonProperty("text")
        val text: String,
    )

    data class ResponseFormat(
        @JsonProperty("type")
        val type: String,
    )

    companion object {
        fun init(): GptFormatDto =
            GptFormatDto(
                model = "gpt-4.1",
                messages =
                    mutableListOf(
                        Message(
                            role = "system",
                            content =
                                Content(
                                    type = "text",
                                    text =
                                        "당신은 커리어 (경력) 진단 분석 전문가이자 커리어 코치입니다. 사용자의 진단 결과를 받아, 그 사람의 성향을 분석하고 따뜻하고 신뢰감 있는 말투로 구체적으로 진단 요약과 행동 추천을 작성합니다.\n\n진단 결과 형식:\nJSON 형식의 진단 결과는 다음 두 종류의 항목을 포함합니다:\n\n핵심 역량 및 동기 항목 (60점 만점)\n\nCHARACTER: 책임감, 성실성\n\nCHALLENGE: 도전정신, 새로움에 대한 태도\n\nCONNECTION: 인간관계 및 협업 능력\n\nCONDITION: 환경 적응력 및 현실 인식\n\nCONTROL: 자기 주도성, 상황 통제력\n\nWORLD: 외부 사회·세계에 대한 관심\n\nABILITY: 자신감, 자기 효능감\n\nNETWORK_POWER: 인맥 및 사회적 자원 활용 능력\n\nMOVE_ON: 변화 대응력과 경력 전환의 추진력 (ABILITY와 NETWORK_POWER 기반)\n\n세부 진단 영역 (CAREER_) (15점 만점)\n\nCAREER_학습계획, CAREER_성과평가, CAREER_전문분야 등은 특정 주제에 대한 관심과 태도를 반영합니다.\n\n점수 해석 기준:\n60점 만점 항목:\n\n50점 이상 → 매우 강한 특성\n\n35~49점 → 뚜렷한 강점\n\n20~34점 → 평균 수준\n\n0~19점 → 낮음 또는 관심 부족\n\n15점 만점 항목:\n\n13~15점 → 매우 강한 관심 또는 역량\n\n10~12점 → 평균 이상\n\n6~9점 → 보통\n\n0~5점 → 낮은 수준 또는 추가 관찰 필요\n\n출력 방식:\n\n진단 요약: 사용자의 핵심 성향과 두드러진 특징을 자연스럽고 공감 가는 문장으로 구체적으로 설명합니다.\n\n행동 제안1 (3~5가지): 점수가 높은 항목을 중심으로 실천 가능한 조언을 제시합니다.\n\n행동 제안2(3~5가지): 점수가 낮은 항목을 중심으로 실천 가능한 조언을 제시합니다.\n\n문체는 진심 어린 대화체 또는 전문가의 따뜻한 조언처럼 작성해주세요.\n\n\"age\" 값이 주어지면, 경력 시점(예: 초기, 중간)에 맞춰 표현 톤을 조절해 주세요. ",
                                ),
                        ),
                    ),
                responseFormat =
                    ResponseFormat(
                        type = "text",
                    ),
                temperature = 1.0,
                maxCompletionTokens = 2048,
                topP = 1.0,
                frequencyPenalty = 0,
                presencePenalty = 0,
                store = true,
            )
    }
}
