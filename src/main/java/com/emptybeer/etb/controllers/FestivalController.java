package com.emptybeer.etb.controllers;

import com.emptybeer.etb.entities.bbs.FestivalArticleEntity;
import com.emptybeer.etb.entities.bbs.FestivalCommentEntity;
import com.emptybeer.etb.entities.bbs.ImageEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.models.PagingModel;
import com.emptybeer.etb.models.PagingModelFestival;
import com.emptybeer.etb.services.BbsService;
import com.emptybeer.etb.services.DataService;
import com.emptybeer.etb.services.FestivalService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller(value = "com.emptybeer.etb.controllers.FestivalController")
@RequestMapping(value = "/festival")
public class FestivalController {

    private final FestivalService festivalService;

    private final BbsService bbsService;
    private final DataService dataService;



    @Autowired
    public FestivalController(FestivalService festivalService, BbsService bbsService, DataService dataService) {
        this.festivalService = festivalService;
        this.bbsService = bbsService;
        this.dataService = dataService;

    }

    // festival 관련

    //festovalRead의 페이지를 가져옴
    @GetMapping(value = "festivalRead")
    public ModelAndView getFestivalRead(@SessionAttribute(value = "user", required = false) UserEntity user,
                                        @RequestParam(value = "index") Integer index,
                                        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page)

    {
        ModelAndView modelAndView = new ModelAndView("festival/festivalRead");

        // festival에 관한 정보를 가져온다.(이름, 날짜, 장소, 위치 ...)
        modelAndView.addObject("festivalArticles", this.festivalService.getFestivalArticleByIndex(index));

        // 댓글 페이징을 처리하기 위한 처리
        int totalCount = this.festivalService.getALLFestivalCommentCountByFestivalArticleIndex(index);
        PagingModel paging = new PagingModel(totalCount, page);
        modelAndView.addObject("paging", paging);

        // festival 게시판의 댓글을 가저온다(festival게시판 index 기준)
        modelAndView.addObject("festivalComments", this.festivalService.getFestivalCommentByArticleIndex(index, paging));


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

        JSONObject responseObject = new JSONObject();
        if (user == null) {
            responseObject.put("result", CommonResult.FAILURE.name().toLowerCase());
        } else {
            Enum<?> result = this.festivalService.deleteFestivalComment(festivalComment, user);
            responseObject.put("result", result.name().toLowerCase());
        }



        return responseObject.toString();
    }


    @PatchMapping(value="festivalComment")
    @ResponseBody
    public String patchFestivalComment(@SessionAttribute(value = "user", required = false) UserEntity user,
                                       @RequestParam(value="index") int index,
                                       @RequestParam(value="content") String content,
                                       FestivalCommentEntity festivalComment){
        //키 밸류 값으로 전달하므로 JSON 오브젝트로 반환한다.
        JSONObject responseObject = new JSONObject();

        if(user==null){
            responseObject.put("result", CommonResult.FAILURE.name().toLowerCase());
        } else{
            Enum<?> result = this.festivalService.updateFestivalComment(festivalComment, user);
            responseObject.put("result", result.name().toLowerCase());
        }

        return responseObject.toString();

    }


    @GetMapping(value="festivalModify")
    @ResponseBody
    public ModelAndView getFestivalModify(@RequestParam("index") int index,
                                          @RequestParam("content")String content)
    {
        // 수정을 하기 위해 거쳐가는 페이지

        int commentIndex = this.festivalService.updateFestivalComment(index, content);

        ModelAndView modelAndView = new ModelAndView("redirect:/festival/festivalRead");

        modelAndView.addObject("index", commentIndex);

        return modelAndView;
    }



    //  Admin 관련


    // 이미지의 갯수만큼 grid로 나열하기 위해 bbsService맵퍼를 이용
    @GetMapping(value = "festivalAdminFestivalRead",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getFestivalAdminFestivalRead(@RequestParam(value = "page", required = false, defaultValue = "1")
                                                         Integer page) {
        ModelAndView modelAndView = new ModelAndView("festival/festivalAdminFestivalRead");


        int totalCount = this.festivalService.getALLFestivalArticle();
        PagingModelFestival paging = new PagingModelFestival(totalCount, page);
        modelAndView.addObject("paging", paging);
        modelAndView.addObject("festivalArticles", this.festivalService.getFestivalArticleInAdmin(paging));


        return modelAndView;
    }


    //이미지를 위해 다중매핑을 사용한다.

    @GetMapping(value="image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@RequestParam(value = "index")int index) throws IOException {

        ImageEntity image = this.festivalService.getImage(index);
        HttpHeaders headers = new HttpHeaders();

        // 헤더의 컨텐트 타입이 무엇을 받을지를 결정한다.(추후 해당 키워드 검색)
        headers.add("Content-Type", image.getFileMime());
        return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
    }



    //페스티벌 입력

    //페이지 렌더링
    @GetMapping(value="festivalAdminFestivalWrite")
    public ModelAndView getFestivalAdminFestivalWrite(){
        ModelAndView modelAndView = new ModelAndView("festival/festivalAdminFestivalWrite");

        return modelAndView;
    }




    // 이미지를 업로드를 하는 동안 많은 시행착오가 있었지만 해결하였으며, 이를 해결한 과정을 살펴보자면 먼저 이미지를 전송할 때 하나의 파일만 전송하였는데,
    // 매개변수를 살펴보면 MultipartFile[]로 배열형태로 되어있었다. 이를 MultipartFile로 변형하였고, 그 다음 entity의 타입과 동일한 변수명인 titleImage를
    // titleImageTemp로 수정하여 변수명을 변형시킨다음 값을 festivalArticle에 대입하였더니 문제가 해결이 되었다.
    @PostMapping(value="festivalAdminFestivalWrite",
    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postFestivalAdminFestivalWrite(@SessionAttribute(value = "user", required = false)UserEntity user,
                                                 @RequestParam(value="titleImageTemp", required = false) MultipartFile titleImage,
                                                 @RequestParam(value="dateFromStr", required = false) String dateFrom,
                                                 @RequestParam(value="dateToStr", required = false) String dateTo,
                                                 @RequestParam(value="timeFromStr", required = false) String timeFrom,
                                                 @RequestParam(value="timeToStr", required = false) String timeTo,
                                                 FestivalArticleEntity festivalArticle) throws IOException, ParseException, IllegalArgumentException {

        // 시간관련 업로드를 작업하였는데, @RequestParam과 entity의 변수명이 같아 충돌이 발생하여 js와 back단에서 변수명을 수정하여 첫번째 문제를 해결하였다.

        // 시간관련 타입에서 발생한 문제는 먼저 js단에서 넘기는 데이터들은 String이라는 점에서 매개변수에서는 String형으로 설정을 먼저 해놓고,
        // back단에서 각각 Date와 Time타입으로 변형하였다.

        // 문자열을 시간관련 타입으로 변형하기 위해서는 SimpleDateFormat이라는 클래스로 반환할 형태를 지정하고 이를 사용하면 변형이 가능하다.

        // 단 Date가 아닌 Time타입에서는 적용이 안되는 문제가 발생하였는데 이를 해결하기 위해서는
        // entity의 해당변수를 date타입으로 변형하는 방법과 Date타입에서 time을 가져와 새롭게 time형으로 설정하는 방법이 있다.
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");


        Date dateF = simpleDateFormat.parse(dateFrom);
        Date dateT = simpleDateFormat.parse(dateTo);

        Date timeF = simpleTimeFormat.parse(timeFrom);
        Date timeT = simpleTimeFormat.parse(timeTo);

        festivalArticle.setDateFrom(dateF);
        festivalArticle.setDateTo(dateT);
        festivalArticle.setTimeFrom(new Time(timeF.getTime()));
        festivalArticle.setTimeTo(new Time(timeT.getTime()));
        festivalArticle.setTitleImage(titleImage.getBytes());



        JSONObject responseObject = new JSONObject();

        if(user==null){
            responseObject.put("result", CommonResult.FAILURE.name().toLowerCase());
        } else{
            Enum<?> result = this.festivalService.festivalWrite(festivalArticle);
            responseObject.put("result", result.name().toLowerCase());
        }


        return responseObject.toString();
    }



    //  페스티벌 수정
    @GetMapping(value="festivalAdminFestivalModify")
    @ResponseBody
    public ModelAndView getFestivalAdminFestivalModify(@SessionAttribute(value = "user", required = false) UserEntity user,
                                                       @RequestParam(value = "index") Integer index){
        ModelAndView modelAndView = new ModelAndView("festival/festivalAdminFestivalModify");

        modelAndView.addObject("festivalArticles", this.festivalService.getFestivalArticleByIndex(index));

        return modelAndView;
    }

    @PatchMapping(value="festivalAdminFestivalModify")
    @ResponseBody
    public String patchFestivalAdminFestivalModify(@SessionAttribute(value = "user", required = false) UserEntity user,
                                                  @RequestParam(value="titleImageTemp", required = false) MultipartFile titleImage,
                                                  @RequestParam(value="dateFromStr", required = false) String dateFrom,
                                                  @RequestParam(value="dateToStr", required = false) String dateTo,
                                                  @RequestParam(value="timeFromStr", required = false) String timeFrom,
                                                  @RequestParam(value="timeToStr", required = false) String timeTo,
                                                  FestivalArticleEntity festivalArticle) throws IOException, ParseException, IllegalArgumentException {

        System.out.println("인덱스는"+festivalArticle.getIndex());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");


        Date dateF = simpleDateFormat.parse(dateFrom);
        Date dateT = simpleDateFormat.parse(dateTo);

        Date timeF = simpleTimeFormat.parse(timeFrom);
        Date timeT = simpleTimeFormat.parse(timeTo);

        festivalArticle.setDateFrom(dateF);
        festivalArticle.setDateTo(dateT);
        festivalArticle.setTimeFrom(new Time(timeF.getTime()));
        festivalArticle.setTimeTo(new Time(timeT.getTime()));

        if(titleImage != null){
            festivalArticle.setTitleImage(titleImage.getBytes());
        }


        JSONObject responseObject = new JSONObject();

        if(user==null){
            responseObject.put("result", CommonResult.FAILURE.name().toLowerCase());
        } else{
            Enum<?> result = this.festivalService.festivalUpdate(festivalArticle);
            responseObject.put("result", result.name().toLowerCase());
        }


        return responseObject.toString();

    }




    // 페스티벌 삭제

    @DeleteMapping(value="festivalAdminFestivalRead")
    @ResponseBody
    public String deleteFestivalAdminFestivalRead(@SessionAttribute(value = "user", required = false) UserEntity user,
                                        FestivalArticleEntity festivalArticle){

        JSONObject responseObject = new JSONObject();

        if (user == null) {
            responseObject.put("result", CommonResult.FAILURE.name().toLowerCase());
        } else {
            Enum<?> result = this.festivalService.deleteFestivalArticle(festivalArticle, user);
            responseObject.put("result", result.name().toLowerCase());
        }



        return responseObject.toString();
    }

}

