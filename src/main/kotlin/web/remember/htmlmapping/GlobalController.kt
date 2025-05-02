package web.remember.htmlmapping

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class GlobalController {
    @GetMapping("/oauth2")
    fun oauth2(): String = "forward:/oauth2.html"

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
