package com.google.googleanalytics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchMainController {

    @GetMapping("SearchMain")
    public String SearchMain() {
        return "SearchMain";
    }

}
