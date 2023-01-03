package com.emptybeer.etb.controllers;

import com.emptybeer.etb.entities.bbs.FestivalCommentEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.services.BbsService;
import com.emptybeer.etb.services.DataService;
import com.emptybeer.etb.services.FestivalService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "com.emptybeer.etb.controllers.FestivalController")
@RequestMapping(value = "/festival")
public class FestivalController {

    private final FestivalService festivalService;

    @Autowired
    public FestivalController(FestivalService festivalService) {
        this.festivalService = festivalService;

    }

    // festival 관련
    @GetMapping(value = "festivalRead")
    public ModelAndView getFestivalRead(@SessionAttribute(value = "user", required = false) UserEntity user,
                                        @RequestParam(value = "index") Integer index) {
        ModelAndView modelAndView = new ModelAndView("festival/festivalRead");

        modelAndView.addObject("festivalArticles", this.festivalService.getFestivalArticleByIndex(index));
        modelAndView.addObject("festivalComments", this.festivalService.getFestivalCommentByArticleIndex(index));

        return modelAndView;
    }


    // festival리뷰 관련

    @PostMapping(value = "festivalRead")
    @ResponseBody
    public String postFestivalRead(@SessionAttribute(value = "user", required = false) UserEntity user,
                                   @RequestParam(value = "articleIndex", required = false) int aid,
                                   @RequestParam(value = "content", required = false) String content,
                                   FestivalCommentEntity festivalComment) {


        JSONObject responseObject = new JSONObject();
        if (user == null) {
            responseObject.put("result", CommonResult.FAILURE.name().toLowerCase());
        } else {
            festivalComment.setUserEmail(user.getEmail());
            festivalComment.setArticleIndex(aid);
            festivalComment.setContent(content);
            Enum<?> result = this.festivalService.writeFestivalComment(festivalComment);
            responseObject.put("result", result.name().toLowerCase());

        }


        return responseObject.toString();
    }
}
