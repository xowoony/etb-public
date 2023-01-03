package com.emptybeer.etb.controllers;

import com.emptybeer.etb.entities.bbs.BasicArticleEntity;
import com.emptybeer.etb.entities.bbs.BoardEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.enums.bbs.WriteResult;
import com.emptybeer.etb.services.BasicBbsService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "com.emptybeer.etb.controllers.BasicBbsController")
@RequestMapping(value = "/basicBbs")
public class BasicBbsController {
    private final BasicBbsService basicBbsService;

    @Autowired
    public BasicBbsController(BasicBbsService basicBbsService) {
        this.basicBbsService = basicBbsService;
    }

    // 글쓰기 게시판
    @GetMapping(value = "write",
    produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getWrite(@SessionAttribute(value = "user", required = false) UserEntity user, @RequestParam(value = "bid", required = false) String bid) {
        ModelAndView modelAndView;

        if (user == null) { // 로그인이 안 되어 있을 때
            modelAndView = new ModelAndView("redirect:/member/login");
        } else {    // 로그인이 되어 있을 때
            modelAndView = new ModelAndView("basicBbs/write");

            if (bid == null || this.basicBbsService.getBoard(bid) == null) {
                modelAndView.addObject("result", CommonResult.FAILURE.name());
            } else {
                modelAndView.addObject("result", CommonResult.SUCCESS.name());

                // html 제목에 연결되는 부분 (자유게시판, Q&A, 공지사항)
                BoardEntity board = this.basicBbsService.getBoard(bid);
                modelAndView.addObject("board", board.getText());
                modelAndView.addObject("bid", board.getId());
            }
        }
        return modelAndView;
    }

    // 글 적기
    @PostMapping(value = "write", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postWrite(String bid, @SessionAttribute(value = "user", required = false) UserEntity user, BasicArticleEntity basicArticle) {

        Enum<?> result;
        int index = 0;
        JSONObject responseObject = new JSONObject();

        if (user == null) {
            result = WriteResult.NOT_ALLOWED;
        } else if (bid == null) {
            result = WriteResult.NO_SUCH_BOARD;

        } else {
            basicArticle.setUserEmail(user.getEmail());
            basicArticle.setBoardId(bid);

            result = this.basicBbsService.write(basicArticle);
            if (result == CommonResult.SUCCESS) {
                responseObject.put("aid", basicArticle.getIndex());
            }
        }

        responseObject.put("result", result.name().toLowerCase());
        responseObject.put("aid", basicArticle.getIndex());
        return responseObject.toString();
    }




}
