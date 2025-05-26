package web.remember.htmlmapping

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class GlobalController {
    @GetMapping("/mypage")
    fun mypage(
        @RequestParam(required = false) testId: String?,
    ): String = "forward:/mypage.html"

    @GetMapping("/error")
    fun error(): String = "forward:/error.html"

    @GetMapping("/career/result/payment")
    fun careerPayment(): String = "forward:/career/careerpayment.html"

    @GetMapping("/oauth2")
    fun oauth2(): String = "forward:/oauth2.html"

    @GetMapping("/privacy-policy")
    fun privacyPolicy(): String = "forward:/privacy-policy.html"

    @GetMapping("/login")
    fun index(): String = "forward:/login.html"

    @GetMapping("/home")
    fun home(): String = "forward:/home.html"

    @GetMapping("/career")
    fun career(): String = "forward:/career.html"

    @GetMapping("/career/result")
    fun careerResult(): String = "forward:/career/result.html"

    @GetMapping("/login-success")
    fun loginSuccess(): String = "forward:/login-success.html"
}
