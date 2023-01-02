package com.emptybeer.etb.controllers;

import com.emptybeer.etb.entities.data.BeerEntity;
import com.emptybeer.etb.services.BbsService;
import com.emptybeer.etb.services.DataService;
import com.emptybeer.etb.vos.BeerVo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller("com.emptybeer.etb.controllers.DataController")
@RequestMapping(value = "/data")
public class DataController {
    // 의존성 주입
    private final DataService dataService;
    private final BbsService bbsService;

    public DataController(DataService dataService, BbsService bbsService) {
        this.dataService = dataService;
        this.bbsService = bbsService;
    }

    @GetMapping(value = "beer",
            produces = MediaType.TEXT_HTML_VALUE)
//    public ModelAndView getBeer(@RequestParam(value = "beerIndex", required = false) int beerIndex) {
    public ModelAndView getBeer() {
        ModelAndView modelAndView = new ModelAndView("data/beer");
        BeerVo[] beers = this.dataService.getBeer();
        modelAndView.addObject("beers", beers);
        return modelAndView;
    }

    @GetMapping(value = "beerImage")
    public ResponseEntity<byte[]> getBeerImage(@RequestParam(value = "beerIndex") int beerIndex) {
        ResponseEntity<byte[]> responseEntity;
        BeerVo beer = this.dataService.getBeerImage(beerIndex);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(beer.getImageType()));
        headers.setContentLength(beer.getImage().length);
        responseEntity = new ResponseEntity<>(beer.getImage(), headers, HttpStatus.OK);

        return responseEntity;
    }


}
