package web.remember.htmlmapping

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class GlobalController {
    @GetMapping("/home")
    fun home(): String = "forward:/home.html"

    @GetMapping("/career")
    fun career(): String = "forward:/career.html"

    @GetMapping("/career/result")
    fun careerResult(): String = "forward:/career/result.html"
}
