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
        responseObject.put("result", result.name().toLowerCase());
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
        responseObject.put("result", result.name().toLowerCase());
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
        BeerVo beer = this.bbsService.getBeer(beerIndex);
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
                             @RequestParam(value = "aid") int aid) {
        ReviewArticleVo reviewArticle = new ReviewArticleVo();
        reviewArticle.setIndex(aid);
        Enum<?> result = this.bbsService.deleteReview(reviewArticle, user);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }



//    // 리뷰 읽기
//    @GetMapping(value = "reviewList",
//            produces = MediaType.TEXT_HTML_VALUE)
//    @ResponseBody
//    public String getReviewRead(@SessionAttribute(value = "user", required = false) UserEntity user,
//                                @RequestParam(value = "beerIndex") int beerIndex) {
//        JSONArray responseArray = new JSONArray();
//        ReviewArticleVo[] reviewArticles = this.bbsService.getReviewArticles(beerIndex, user);
//
//        for (ReviewArticleVo reviewArticle : reviewArticles) {
//            JSONObject reviewObject = new JSONObject();
//            reviewObject.put("index", reviewArticle.getIndex());
//            reviewObject.put("userEmail", reviewArticle.getUserEmail());
//            reviewObject.put("beerIndex", reviewArticle.getBeerIndex());
//            reviewObject.put("userNickname", reviewArticle.getUserNickname());
//            reviewObject.put("score", reviewArticle.getScore());
//            reviewObject.put("contentGood", reviewArticle.getContentGood());
//            reviewObject.put("contentBad", reviewArticle.getContentBad());
//            reviewObject.put("modifiedOn", new SimpleDateFormat("yyyy.MM.dd").format(reviewArticle.getModifiedOn()));
//            // new SimpleDataFormat("형식").format([Date 타입 객체]) : [Date 타입 객체]가 가진 일시를 원하는 형식의 문자열로 만들어 버린다.
//            reviewObject.put("isSigned", true);    // 로그인이 되어 있으면 true
//            reviewObject.put("isMine", user.getEmail().equals(reviewArticle.getUserEmail())); // 로그인이 되어 있고, 그 유저가
//            reviewObject.put("isLiked", reviewArticle.isLiked());
//            reviewObject.put("likeCount", reviewArticle.getLikeCount());
//            responseArray.put(reviewObject);
//
//        }
//
//        return responseArray.toString();
//    }



}
