// package web.remember.domain.member.application
//
// import org.springframework.security.core.userdetails.UserDetails
// import org.springframework.security.core.userdetails.UserDetailsService
// import org.springframework.stereotype.Service
// import web.remember.domain.error.CustomException
// import web.remember.domain.member.repository.MemberRepository
// import web.remember.domain.member.security.PrincipalDetails
//
// @Service
// class MemberDetailsServiceImpl(
//    private val memberRepository: MemberRepository,
// ) : MemberDetailsService,
//    UserDetailsService {
//    private val logger = org.slf4j.LoggerFactory.getLogger(MemberDetailsServiceImpl::class.java)
//
//    override fun loadUserByPhoneNumber(phoneNumber: String): UserDetails = loadUserByUsername(phoneNumber)
//
//    override fun loadUserByUsername(phoneNumber: String?): UserDetails {
//        if (phoneNumber == null) {
//            logger.error("Phone number is null")
//            throw CustomException("Phone number cannot be null")
//        }
//        logger.info("Loading user by phone number: $phoneNumber")
//        val member =
//            memberRepository.findByPhoneNumber(phoneNumber)
//                ?: throw CustomException("Member not found with phone number: $phoneNumber")
//        return PrincipalDetails(member)
//    }
// }
