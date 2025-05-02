// package web.remember.domain.member.application
//
// import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
// import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
// import org.springframework.security.oauth2.core.user.OAuth2User
// import org.springframework.stereotype.Service
// import web.remember.domain.member.entity.Member
// import web.remember.domain.member.repository.MemberRepository
// import web.remember.domain.member.security.PrincipalDetails
//
// @Service
// class Oauth2MemberService(
//    private val memberRepository: MemberRepository,
// ) : DefaultOAuth2UserService() {
//    val logger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(Oauth2MemberService::class.java)
//
//    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
//        if (userRequest == null) {
//            logger.error("User request is null")
//            throw IllegalArgumentException("User request cannot be null")
//        }
//        logger.info("getClientRegistrationId: ${userRequest.clientRegistration}")
//        logger.info("getAccessToken: ${userRequest.accessToken.tokenValue}")
//
//        val oAuth2User = super.loadUser(userRequest)
//        logger.info("OAuth2User Attribute: ${oAuth2User.attributes}")
//        val provider = userRequest.clientRegistration.registrationId
//        val providerId = oAuth2User.attributes["id"].toString() as String
//        val obj = oAuth2User.attributes["kakao_account"] as Map<*, *>
//        val email = obj["email"] as String
//        val name = oAuth2User.attributes["name"] as String
//        val role = "ROLE_USER"
//
//        val memberEntity = memberRepository.findByEmailL(email)
//        if (memberEntity == null) {
//            // kakao Login 최초
//            val member =
//                Member(
//                    name = name,
//                )
//            member.emailL = email
//            memberRepository.save(member)
//            return PrincipalDetails(member, oAuth2User.attributes)
//        }
//        return PrincipalDetails(memberEntity, oAuth2User.attributes)
//    }
// }
