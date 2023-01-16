package com.emptybeer.etb.controllers;

import com.emptybeer.etb.entities.bbs.FestivalArticleEntity;
import com.emptybeer.etb.entities.data.BeerEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.models.PagingModel;
import com.emptybeer.etb.models.PagingModelBeer;
import com.emptybeer.etb.models.PagingModelFestival;
import com.emptybeer.etb.services.BbsService;
import com.emptybeer.etb.services.DataService;
import com.emptybeer.etb.vos.BeerVo;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public ModelAndView getBeer(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "criterion", required = false) String criterion,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "beerCategory", required = false) String beerCategory) {

        page = Math.max(1, page);
        ModelAndView modelAndView = new ModelAndView("data/beer");
        int totalCount = this.dataService.getBeerCount(criterion, keyword, beerCategory);
        PagingModelBeer paging = new PagingModelBeer(totalCount, page);
        modelAndView.addObject("paging", paging);
        BeerVo[] beers = this.dataService.getBeer(paging, criterion, keyword, beerCategory);
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


    // 관리자페이지 맥주관련

    @GetMapping(value = "beerAdmin")
    public ModelAndView getBeerAdmin(@RequestParam(value = "page", required = false, defaultValue = "1")
                                         Integer page) {
        ModelAndView modelAndView = new ModelAndView("data/beerAdmin");

        int totalCount = this.dataService.getBeerForPageCount();
        PagingModelBeer paging = new PagingModelBeer(totalCount, page);
        modelAndView.addObject("paging", paging);
        // 관리자 페이지용 맥주 정보를 가져온다.
        modelAndView.addObject("beers", this.dataService.getBeerForAdmin(paging));

        return modelAndView;
    }


    // 관리자 페이지 맥주 입력
    @GetMapping(value = "beerAdminWrite")
    public ModelAndView getBeerAdminWrite() {
        ModelAndView modelAndView = new ModelAndView("data/beerAdminWrite");

        return modelAndView;
    }

    @PostMapping(value = "beerAdminWrite")
    @ResponseBody
    public String postFestivalAdminFestivalWrite(@SessionAttribute(value = "user", required = false) UserEntity user,
                                                 @RequestParam(value = "beerImage", required = false) MultipartFile image,
                                                 BeerEntity beer) throws IOException, ParseException, IllegalArgumentException {


        beer.setImage(image.getBytes());

        JSONObject responseObject = new JSONObject();

        if (user == null) {
            responseObject.put("result", CommonResult.FAILURE.name().toLowerCase());
        } else {
            Enum<?> result = this.dataService.beerWrite(beer);
            responseObject.put("result", result.name().toLowerCase());
        }

        return responseObject.toString();
    }


    // 관리자 페이지 맥주 수정

    @GetMapping(value = "beerAdminModify")
    @ResponseBody
    public ModelAndView getBeerAdminModify(@SessionAttribute(value = "user", required = false) UserEntity user,
                                           @RequestParam(value = "index") Integer index) {
        ModelAndView modelAndView = new ModelAndView("data/beerAdminModify");

        modelAndView.addObject("beers", this.dataService.getBeerByIndex(index));

        return modelAndView;
    }

    @PatchMapping(value = "beerAdminModify")
    @ResponseBody
    public String patchBeerAdminModify(
            @SessionAttribute(value = "user", required = false) UserEntity user,
            @RequestParam(value = "beerImage", required = false) MultipartFile image,
            BeerEntity beer) throws IOException, ParseException, IllegalArgumentException {



        JSONObject responseObject = new JSONObject();

        if (user == null) {
            responseObject.put("result", CommonResult.FAILURE.name().toLowerCase());
        } else {
            Enum<?> result = this.dataService.beerUpdate(beer, image);
            responseObject.put("result", result.name().toLowerCase());
        }

        return responseObject.toString();
    }


    @DeleteMapping(value="beerAdmin")
    @ResponseBody
    public String deleteBeerAdmin(@SessionAttribute(value="user", required = false)UserEntity user,
                                  BeerEntity beer){

        JSONObject responseObject = new JSONObject();

        if (user == null) {
            responseObject.put("result", CommonResult.FAILURE.name().toLowerCase());
        } else {
            Enum<?> result = this.dataService.deleteBeer(beer, user);
            responseObject.put("result", result.name().toLowerCase());
        }


        return responseObject.toString();
    }

}
