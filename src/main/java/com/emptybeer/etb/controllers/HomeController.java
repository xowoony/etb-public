package com.emptybeer.etb.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller(value="com.emptybeer.etb.controllers.HomeController")
@RequestMapping(value="/")
public class HomeController {
    @GetMapping(value="/",
    produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getIndex() {
        ModelAndView modelAndView = new ModelAndView("home/index");
        return modelAndView;
    }

    @GetMapping(value="product",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getProduct() {
        ModelAndView modelAndView = new ModelAndView("home/product");
        return modelAndView;
    }


    @GetMapping(value="community",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getCommunity() {
        ModelAndView modelAndView = new ModelAndView("home/community");
        return modelAndView;
    }

    @GetMapping(value="festival",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getFestival() {
        ModelAndView modelAndView = new ModelAndView("home/festival");
        return modelAndView;
    }
}
