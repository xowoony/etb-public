package com.emptybeer.etb.controllers;

import com.emptybeer.etb.entities.bbs.*;
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
    public ModelAndView getReviewWrite(@SessionAttribute(value = "user", required = false) UserEntity user, @RequestParam(value = "bid", required = false) String bid) {
        ModelAndView modelAndView;

        if (user == null) { // 로그인이 안 되어 있을 때
            modelAndView = new ModelAndView("redirect:/member/login");
        } else {    // 로그인이 되어 있을 때
            modelAndView = new ModelAndView("bbs/reviewWrite");

            if (bid == null || this.bbsService.getBoard(bid) == null) {
                modelAndView.addObject("result", CommonResult.FAILURE.name());
            } else {
                modelAndView.addObject("result", CommonResult.SUCCESS.name());

                // html 제목에 연결되는 부분 (자유게시판, Q&A, 공지사항)
                BoardEntity board = this.bbsService.getBoard(bid);
//                modelAndView.addObject("board", board.getText());
                modelAndView.addObject("bid", board.getId());
            }
        }

        return modelAndView;
    }


    // 리뷰 작성
    @PostMapping(value = "reviewWrite",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postReviewWrite(String bid, @SessionAttribute(value = "user", required = false) UserEntity user, ReviewArticleEntity reviewArticle) {
        // SessionAttribute 는 쿠키 (로그인 되어 있을 떄만 어쩌구)

        Enum<?> result;
        int index = 0;
        JSONObject responseObject = new JSONObject();

        if (user == null) {
            result = WriteResult.NOT_ALLOWED;
        } else if (bid == null) {
            result = WriteResult.NO_SUCH_BOARD;

        } else {
            reviewArticle.setUserEmail(user.getEmail());
            reviewArticle.setBoardId(bid);

            result = this.bbsService.reviewWrite(reviewArticle);
            if (result == CommonResult.SUCCESS) {
                responseObject.put("aid", reviewArticle.getIndex());
            }
        }

        responseObject.put("result", result.name().toLowerCase());
        responseObject.put("aid", reviewArticle.getIndex());
        return responseObject.toString();
    }
}
