package com.emptybeer.etb.controllers;

import com.emptybeer.etb.entities.bbs.BasicArticleLikeEntity;
import com.emptybeer.etb.entities.bbs.ReviewArticleLikeEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.services.MemberBbsService;
import com.emptybeer.etb.services.MemberService;
import com.emptybeer.etb.vos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller(value = "com.emptybeer.etb.controllers.MemberBbsController")
@RequestMapping(value = "/memberBbs")
public class MemberBbsController {
    private final MemberBbsService memberBbsService;
    private final MemberService memberService;

    @Autowired
    public MemberBbsController(MemberBbsService memberBbsService, MemberService memberService) {
        this.memberBbsService = memberBbsService;
        this.memberService = memberService;
    }

    // 작성한 글 리스트
    @GetMapping(value = "myArticle",
    produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMyArticle(@SessionAttribute(value = "user", required = false) UserEntity user, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("memberBbs/myArticle");
        if (user != null) {
            session.setAttribute("userNickname", user.getNickname());
            session.setAttribute("userEmail", user.getEmail());
            BasicArticleVo[] basicArticles = this.memberBbsService.getMyArticles(user);
            modelAndView.addObject("basicArticles", basicArticles);
            ReviewArticleVo[] reviewArticles = this.memberBbsService.getMyReviews(user);
            modelAndView.addObject("reviewArticles", reviewArticles);
        }
        return modelAndView;
    }

    // 작성 댓글 리스트
    @GetMapping(value = "myComment",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMyComment(@SessionAttribute(value = "user", required = false) UserEntity user, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("memberBbs/myComment");
        if (user != null) {
            session.setAttribute("userNickname", user.getNickname());
            session.setAttribute("userEmail", user.getEmail());
            BasicCommentVo[] basicComments = this.memberBbsService.getMyComments(user);
            modelAndView.addObject("basicComments", basicComments);
        }
        return modelAndView;
    }

    // 좋아요 한 게시글 리스트
    @GetMapping(value = "myLike",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMyLikeArticle(@SessionAttribute(value = "user", required = false) UserEntity user, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("memberBbs/myLike");
        if (user != null) {
            session.setAttribute("userNickname", user.getNickname());
            session.setAttribute("userEmail", user.getEmail());
            BasicArticleLikeVo[] basicArticleLikes = this.memberBbsService.getMyLikeArticles(user);
            modelAndView.addObject("basicArticleLikes", basicArticleLikes);
            ReviewArticleLikeVo[] reviewArticleLikes = this.memberBbsService.getMyLikeReviews(user);
            modelAndView.addObject("reviewArticleLikes", reviewArticleLikes);
        }
        return modelAndView;
    }
}
