package com.emptybeer.etb.controllers;

import com.emptybeer.etb.entities.bbs.*;
import com.emptybeer.etb.entities.data.*;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.enums.bbs.WriteResult;
import com.emptybeer.etb.models.PagingModel;
import com.emptybeer.etb.services.BbsService;
import com.emptybeer.etb.services.DataService;
import com.emptybeer.etb.vos.BeerVo;
import com.emptybeer.etb.vos.ReviewArticleVo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "com.emptybeer.etb.controllers.BbsController")
@RequestMapping(value = "/bbs")
public class BbsController {

    private final BbsService bbsService;
    private final DataService dataService;

    @Autowired
    public BbsController(BbsService bbsService, DataService dataService) {
        this.bbsService = bbsService;
        this.dataService = dataService;
    }

    //전체 리뷰리스트
    @GetMapping(value = "review",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getReview(
            @SessionAttribute(value = "user", required = false) UserEntity user,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "criterion", required = false) String criterion,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "starRank", required = false) String starRank,
            @RequestParam(value = "sort", required = false) String sort) {
        page = Math.max(1, page);

        ModelAndView modelAndView = new ModelAndView("bbs/review");

        int totalCount = this.bbsService.getALLReviewArticleCount(criterion, keyword, starRank);
        BeerVo allReviewCount = this.bbsService.getAllReviewCount();
        modelAndView.addObject("allReviewCount", allReviewCount);

        PagingModel paging = new PagingModel(totalCount, page);
        modelAndView.addObject("paging", paging);

        ReviewArticleVo[] reviewArticles = this.bbsService.getAllReviewArticles(user, paging, criterion, keyword, starRank, sort);
        modelAndView.addObject("reviewArticles", reviewArticles);
        return modelAndView;
    }


    //리뷰 글쓰기
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


    // 리뷰 맥주 이미지 가져오기
    @GetMapping(value = "beerImage")
    public ResponseEntity<byte[]> getBeerImage(@RequestParam(value = "beerIndex") int beerIndex) {
        ResponseEntity<byte[]> responseEntity;
        BeerEntity beer = this.bbsService.getBeer(beerIndex);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(beer.getImageType()));
        headers.setContentLength(beer.getImage().length);
        responseEntity = new ResponseEntity<>(beer.getImage(), headers, HttpStatus.OK);
        return responseEntity;
    }


    // 리뷰 맥주 좋아요 기능
    @PostMapping(value = "beerLike",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postBeerLike(@SessionAttribute(value = "user", required = false) UserEntity user,
                               BeerLikeEntity beerLike) {
        JSONObject responseObject = new JSONObject();
        Enum<?> result = this.bbsService.beerLike(beerLike, user);
        BeerVo beer = this.bbsService.getBeerLike(beerLike.getBeerIndex(), user);

        responseObject.put("result", result.name().toLowerCase());
        responseObject.put("isLiked", beer.isLiked());
        responseObject.put("likeCount", beer.getLikeCount());
        return responseObject.toString();
    }

    // 리뷰 맥주 좋아요 취소 기능
    @DeleteMapping(value = "beerLike",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteBeerLike(@SessionAttribute(value = "user", required = false) UserEntity user,
                                 BeerLikeEntity beerLike) {
        JSONObject responseObject = new JSONObject();
        Enum<?> result = this.bbsService.beerUnlike(beerLike, user);
        BeerVo beer = this.bbsService.getBeerLike(beerLike.getBeerIndex(), user);

        responseObject.put("result", result.name().toLowerCase());
        responseObject.put("isLiked", beer.isLiked());
        responseObject.put("likeCount", beer.getLikeCount());
        return responseObject.toString();
    }


    // 리뷰 리스트
    @GetMapping(value = "reviewList",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getReviewList(@SessionAttribute(value = "user", required = false) UserEntity user,
                                      @RequestParam(value = "beerIndex") int beerIndex,
                                      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                      @RequestParam(value = "criterion", required = false) String criterion,
                                      @RequestParam(value = "keyword", required = false) String keyword,
                                      @RequestParam(value = "starRank", required = false) String starRank,
                                      @RequestParam(value = "sort", required = false) String sort) {
        page = Math.max(1, page);
        // 또는 if문 사용 가능. page는 1보다 작을 수 없다. 1이랑 page 중에 더 큰 값을 내놔라.
        ModelAndView modelAndView = new ModelAndView("bbs/reviewList");
//        BeerVo beer = this.bbsService.getBeer(beerIndex);
        BeerVo beer = this.bbsService.getBeerLike(beerIndex, user);
        modelAndView.addObject("beer", beer);

        int totalCount = this.bbsService.getReviewArticleCount(beer, criterion, keyword, starRank);
        modelAndView.addObject("totalCount", totalCount);
        double avgReview = this.bbsService.getReviewAvg(beer);
        modelAndView.addObject("avgReview", avgReview);
        BeerVo reviewCount = this.bbsService.getReviewCount(beer);
        modelAndView.addObject("reviewCount", reviewCount);

        // int totalCount = this.bbsService.getArticleCount(board);
        PagingModel paging = new PagingModel(totalCount, page);
        modelAndView.addObject("paging", paging);

        ReviewArticleVo[] reviewArticles = this.bbsService.getReviewArticles(user, beer, paging, criterion, keyword, starRank, sort);
        modelAndView.addObject("reviewArticles", reviewArticles);
        return modelAndView;
    }


    // 리뷰 읽기
    @GetMapping(value = "reviewRead",
            produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ModelAndView getReviewRead(@SessionAttribute(value = "user", required = false) UserEntity user,
                                      @RequestParam(value = "aid") int aid) {
        ModelAndView modelAndView = new ModelAndView("bbs/reviewRead");
        ReviewArticleVo reviewArticle = this.bbsService.reviewReadArticle(user, aid);

        modelAndView.addObject("reviewArticle", reviewArticle);

        return modelAndView;
    }


    // 리뷰 수정하기
    @GetMapping(value = "reviewModify",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getModify(@SessionAttribute(value = "user", required = false) UserEntity user, @RequestParam(value = "aid") int aid) {
        ModelAndView modelAndView = new ModelAndView("bbs/reviewModify");
        ReviewArticleVo reviewArticle = new ReviewArticleVo();
        reviewArticle.setIndex(aid);
        Enum<?> result = this.bbsService.prepareModifyReview(reviewArticle, user);
        modelAndView.addObject("reviewArticle", reviewArticle);
        modelAndView.addObject("result", result.name());
        return modelAndView;
    }


    // 게시글 수정
    @RequestMapping(value = "reviewModify",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchModify(@SessionAttribute(value = "user", required = false) UserEntity user,
                              ReviewArticleVo reviewArticle,
                              @RequestParam(value = "aid") int aid) {
        reviewArticle.setIndex(aid);
        Enum<?> result = this.bbsService.modifyReview(reviewArticle, user);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseObject.put("aid", aid);
        }
        return responseObject.toString();
    }


    // 게시글 삭제
    @DeleteMapping(value = "reviewRead",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteReviewRead(@SessionAttribute(value = "user", required = false) UserEntity user,
                                   @RequestParam(value = "aid") int[] aids) {
        int count = 0;
        for (int aid : aids) {
            ReviewArticleVo reviewArticle = new ReviewArticleVo();
            reviewArticle.setIndex(aid);
            count += this.bbsService.deleteReview(reviewArticle, user) == CommonResult.SUCCESS ? 1 : 0;
        }
        Enum<?> result = count == aids.length
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }

    // 리뷰 좋아요(추천)
    @PostMapping(value = "reviewLike",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postReviewLike(
            @SessionAttribute(value = "user", required = false) UserEntity user,
            ReviewArticleLikeEntity reviewArticleLike,
            @RequestParam(value = "articleIndex", required = false) int aid) {
        JSONObject responseObject = new JSONObject();
        reviewArticleLike.setArticleIndex(aid);
        Enum<?> result = this.bbsService.reviewLike(reviewArticleLike, user);
        ReviewArticleVo reviewArticle = this.bbsService.reviewReadArticle(user, reviewArticleLike.getArticleIndex());

        responseObject.put("result", result.name().toLowerCase());
        responseObject.put("isLiked", reviewArticle.isLiked());
        responseObject.put("likeCount", reviewArticle.getLikeCount());
        return responseObject.toString();
    }

    // 리뷰 좋아요 취소
//    @DeleteMapping(value = "reviewLike",
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public String deleteReviewLike(@SessionAttribute(value = "user", required = false) UserEntity user,
//                                   ReviewArticleLikeEntity reviewArticleLike,
//                                   @RequestParam(value = "aid", required = false) int aid) {
//        JSONObject responseObject = new JSONObject();
//        reviewArticleLike.setArticleIndex(aid);
//        Enum<?> result = this.bbsService.reviewUnlike(reviewArticleLike, user);
//        ReviewArticleVo reviewArticle = this.bbsService.reviewReadArticle(user, reviewArticleLike.getArticleIndex());
//
//        responseObject.put("result", result.name().toLowerCase());
//        responseObject.put("isLiked", reviewArticle.isLiked());
//        responseObject.put("likeCount", reviewArticle.getLikeCount());
//        return responseObject.toString();
//    }
    @DeleteMapping(value = "reviewLike",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteReviewLike(@SessionAttribute(value = "user", required = false) UserEntity user,
                                   ReviewArticleLikeEntity reviewArticleLike,
                                   @RequestParam(value = "articleIndex", required = false) int[] aids){
        Boolean isLiked = null;
        int likeCount = 0;
        int count = 0;
        for (int aid : aids) {
            reviewArticleLike.setArticleIndex(aid);
            count += this.bbsService.reviewUnlike(reviewArticleLike, user) == CommonResult.SUCCESS ? 1 : 0;
            ReviewArticleVo reviewArticle = this.bbsService.reviewReadArticle(user, reviewArticleLike.getArticleIndex());
            isLiked = reviewArticle.isLiked();
            likeCount = reviewArticle.getLikeCount();
        }
        Enum<?> result = count == aids.length
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseObject.put("isLiked", isLiked);
            responseObject.put("likeCount", likeCount);
        }
        return responseObject.toString();
    }



    // 리뷰 신고하기 기능
    @PostMapping(value = "reviewDeclaration",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postReviewDeclaration(
            @SessionAttribute(value = "user", required = false) UserEntity user,
            ReviewArticleDeclarationEntity reviewArticleDeclaration,
            @RequestParam(value = "aid", required = false) int aid) {
        JSONObject responseObject = new JSONObject();
        reviewArticleDeclaration.setArticleIndex(aid);
        Enum<?> result = this.bbsService.reviewDecla(reviewArticleDeclaration, user);
        ReviewArticleVo reviewArticle = this.bbsService.reviewReadArticle(user, reviewArticleDeclaration.getArticleIndex());

        responseObject.put("result", result.name().toLowerCase());
        responseObject.put("isDeclared", reviewArticle.isDeclared());
        return responseObject.toString();
    }

    // 리뷰 신고 초기화
    @DeleteMapping(value = "reviewDeclaration",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteReviewDeclaration(
            @SessionAttribute(value = "user", required = false) UserEntity user,
            @RequestParam(value = "aid", required = false) int[] aids) {
        int count = 0;
        for (int aid : aids) {
            ReviewArticleDeclarationEntity reviewArticleDeclaration = new ReviewArticleDeclarationEntity();
            reviewArticleDeclaration.setArticleIndex(aid);
            count += this.bbsService.resetReport(reviewArticleDeclaration, user) == CommonResult.SUCCESS ? 1 : 0;
        }
        Enum<?> result = count == aids.length ? CommonResult.SUCCESS : CommonResult.FAILURE;
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }


    // 관리자 페이지
    @GetMapping(value = "review-admin",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getReviewAdmin(
            @SessionAttribute(value = "user", required = false) UserEntity user,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "criterion", required = false) String criterion,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sort", required = false) String sort) {
        page = Math.max(1, page);

        ModelAndView modelAndView = new ModelAndView("bbs/reviewAdmin");

        int totalCount = this.bbsService.getReportedReviewCount(criterion, keyword);

        PagingModel paging = new PagingModel(totalCount, page);
        modelAndView.addObject("paging", paging);

        ReviewArticleVo[] reportedReviews = this.bbsService.getReportedReviews(user, paging, criterion, keyword, sort);
        modelAndView.addObject("reportedReviews", reportedReviews);
        return modelAndView;
    }
}
