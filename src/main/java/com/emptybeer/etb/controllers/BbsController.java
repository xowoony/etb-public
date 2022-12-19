package com.emptybeer.etb.controllers;

import com.emptybeer.etb.entities.bbs.*;
import com.emptybeer.etb.entities.data.*;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.enums.bbs.WriteResult;
import com.emptybeer.etb.models.PagingModel;
import com.emptybeer.etb.services.BbsService;
import com.emptybeer.etb.vos.ReviewArticleVo;
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


    //리뷰 글쓰기  >> 한사람당 맥주 리뷰 한개만 가능하게 만들기
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
        JSONObject responseObject = new JSONObject();

        if (user == null) {
            result = WriteResult.NOT_SIGNED;
        } else {
            reviewArticle.setUserEmail(user.getEmail());

            result = this.bbsService.reviewAdd(user, reviewArticle);
            if (result == CommonResult.SUCCESS) {
                responseObject.put("aid", reviewArticle.getIndex());
            }
        }

        responseObject.put("result", result.name().toLowerCase());
        responseObject.put("aid", reviewArticle.getIndex());
        return responseObject.toString();
    }


    // 리뷰 리스트
    @GetMapping(value = "reviewList",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getReviewList(@RequestParam(value = "beerIndex") int beerIndex,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "criterion", required = false) String criterion,
            @RequestParam(value = "keyword", required = false) String keyword) {
        page = Math.max(1, page);
        // 또는 if문 사용 가능. page는 1보다 작을 수 없다. 1이랑 page 중에 더 큰 값을 내놔라.
        ModelAndView modelAndView = new ModelAndView("bbs/reviewList");
        BeerEntity beer = this.bbsService.getBeer(beerIndex);
        modelAndView.addObject("beer", beer);

        int totalCount = this.bbsService.getReviewArticleCount(beer,criterion, keyword);


        // int totalCount = this.bbsService.getArticleCount(board);
        PagingModel paging = new PagingModel(totalCount, page);
        // System.out.printf("이동 가능한 최소 페이지 : %d\n", paging.minPage);
        // System.out.printf("이동 가능한 최대 페이지 : %d\n", paging.maxPage);
        // System.out.printf("표시 시작 페이지 : %d\n", paging.startPage);
        // System.out.printf("표시 끝 페이지 : %d\n", paging.endPage);
        modelAndView.addObject("paging", paging);

        ReviewArticleVo[] reviewArticles = this.bbsService.getReviewArticles(beer, paging, criterion, keyword);
        modelAndView.addObject("reviewArticles", reviewArticles);
        return modelAndView;
    }

}
