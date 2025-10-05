package com.build;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebpageController {
    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
