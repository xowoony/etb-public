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

    //festovalRead의 페이지를 가져옴
    @GetMapping(value = "festivalRead")
    public ModelAndView getFestivalRead(@SessionAttribute(value = "user", required = false) UserEntity user,
                                        @RequestParam(value = "index") Integer index) {
        ModelAndView modelAndView = new ModelAndView("festival/festivalRead");

        // festival에 관한 정보를 가져온다.(이름, 날짜, 장소, 위치 ...)
        modelAndView.addObject("festivalArticles", this.festivalService.getFestivalArticleByIndex(index));

        // festival 게시판의 댓글을 가저온다(festival게시판 index 기준)
        modelAndView.addObject("festivalComments", this.festivalService.getFestivalCommentByArticleIndex(index));

        return modelAndView;
    }


    // festival 댓글 관련

    //festivalRead페이지에서 작성된 글을 입력(insert)
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



    //festivalRead 댓글 삭제

    @DeleteMapping(value="festivalComment")
    @ResponseBody
    public String deleteFestivalComment(@SessionAttribute(value = "user", required = false) UserEntity user,
                                        FestivalCommentEntity festivalComment){
        System.out.println("1111111======="+festivalComment.getIndex());
        Enum<?> result = this.festivalService.deleteFestivalComment(festivalComment, user);


        //키 밸류 값으로 전달하므로 JSON 오브젝트로 반환한다.
        JSONObject responseJson = new JSONObject();

        responseJson.put("result", result.name().toLowerCase());

        return responseJson.toString();
    }
}

