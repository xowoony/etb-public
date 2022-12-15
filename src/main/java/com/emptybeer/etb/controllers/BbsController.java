package com.emptybeer.etb.controllers;

import com.emptybeer.etb.entities.bbs.*;
import com.emptybeer.etb.entities.data.*;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.enums.bbs.WriteResult;
import com.emptybeer.etb.services.BbsService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "com.emptybeer.etb.controllers.BbsController")
@RequestMapping(value = "/bbs")
public class BbsController {

    private final BbsService bbsService;

    @Autowired
    public BbsController(BbsService bbsService) {
        this.bbsService = bbsService;
    }

    @GetMapping(value = "reviewWrite",
    produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getReviewWrite(@SessionAttribute(value = "user", required = false) UserEntity user,
                                       @RequestParam(value = "beerIndex") int beerIndex) {
        ModelAndView modelAndView;

        if (user == null) { // 로그인이 안 되어 있을 때
            modelAndView = new ModelAndView("redirect:/member/login");
        } else {    // 로그인이 되어 있을 때
            modelAndView = new ModelAndView("bbs/reviewWrite");
            BeerEntity beer = this.bbsService.getBeer(beerIndex);

            modelAndView.addObject("beer", beer);
        }
        return modelAndView;
    }


    // 리뷰 작성
    @PostMapping(value = "reviewWrite",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postReviewWrite(@SessionAttribute(value = "user", required = false) UserEntity user, ReviewArticleEntity reviewArticle) {
        // SessionAttribute 는 쿠키 (로그인 되어 있을 떄만 어쩌구)

        Enum<?> result;
        int index = 0;
        JSONObject responseObject = new JSONObject();

        if (user == null) {
            result = WriteResult.NOT_ALLOWED;
        } else {
            reviewArticle.setUserEmail(user.getEmail());

            result = this.bbsService.reviewAdd(reviewArticle);
            if (result == CommonResult.SUCCESS) {
                responseObject.put("aid", reviewArticle.getIndex());
            }
        }

        responseObject.put("result", result.name().toLowerCase());
        responseObject.put("aid", reviewArticle.getIndex());
        return responseObject.toString();
    }
}
