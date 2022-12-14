package com.emptybeer.etb.controllers;

import com.emptybeer.etb.services.BbsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "com.emptybeer.etb.controllers.BbsController")
@RequestMapping(value = "/bbs")
public class BbsController {

    private final BbsService bbsService;

    @Autowired
    public BbsController(BbsService bbsService) {
        this.bbsService = bbsService;
    }

    @GetMapping(value = "/reviewWrite",
    produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getReviewWrite(@RequestParam(value = "bid", required = false) String bid) {
        // sessionAttribute 넣어야 함.
        ModelAndView modelAndView = new ModelAndView("bbs/reviewWrite");
        return modelAndView;
    }
}
