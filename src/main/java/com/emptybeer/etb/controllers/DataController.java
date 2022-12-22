package com.emptybeer.etb.controllers;

import com.emptybeer.etb.services.DataService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/data")
public class DataController {
    // 의존성 주입
    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping(value = "beer",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getBeer() {
        ModelAndView modelAndView = new ModelAndView("data/beer");
        return modelAndView;
    }



}
