package com.emptybeer.etb.controllers;

import com.emptybeer.etb.entities.bbs.*;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.enums.bbs.WriteResult;
import com.emptybeer.etb.models.PagingModel;
import com.emptybeer.etb.services.BasicBbsService;
import com.emptybeer.etb.vos.BasicArticleLikeVo;
import com.emptybeer.etb.vos.BasicArticleVo;
import com.emptybeer.etb.vos.BasicCommentVo;
import com.emptybeer.etb.vos.ReviewArticleVo;
import org.json.JSONArray;
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
import org.thymeleaf.spring5.context.SpringContextUtils;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;

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


    // 이미지
    @PostMapping(value = "image",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postImage(@RequestParam(value = "upload") MultipartFile file) throws IOException {
        ImageEntity image = new ImageEntity();
        image.setFileName(file.getOriginalFilename());
        image.setFileMime(file.getContentType());
        image.setData(file.getBytes());

        Enum<?> result = this.basicBbsService.addImage(image);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseObject.put("url", "../basicBbs/image?id=" + image.getIndex());
        }
        return responseObject.toString();
    }

    // 다운로드용 맵핑
    @GetMapping(value = "image")
    public ResponseEntity<byte[]> getImage(@RequestParam(value = "id") int id) {
        ImageEntity image = this.basicBbsService.getImage(id);
        if (image == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", image.getFileMime());
        // 이미지인지 어쩌구인지 판거
        return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
    }


    // 글 읽기
    @GetMapping(value = "read",
            produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ModelAndView getRead(@RequestParam(value = "aid") int aid,
                                @SessionAttribute(value = "user", required = false) UserEntity user,
                                HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("basicBbs/read");
        BasicArticleVo basicArticle = this.basicBbsService.readArticle(aid, user);

        modelAndView.addObject("basicArticle", basicArticle);
        if (user != null) {
            session.setAttribute("userNickname", user.getNickname());
        }

        // 게시판 이름 맞추는거
        if (basicArticle != null) {  // 글이 있으면
            BoardEntity board = this.basicBbsService.getBoard(basicArticle.getBoardId());
            modelAndView.addObject("board", board);
        }
        return modelAndView;
    }

    // 댓글 작성
    @PostMapping(value = "read",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRead(@SessionAttribute(value = "user", required = false) UserEntity user, BasicCommentEntity basicComment) {

        JSONObject responseObject = new JSONObject();
        if (user == null) {
            responseObject.put("result", CommonResult.FAILURE.name().toLowerCase());
        } else {
            basicComment.setUserEmail(user.getEmail());
            BasicArticleVo basicArticle = this.basicBbsService.readArticle(basicComment.getArticleIndex(), user);
            Enum<?> result = this.basicBbsService.writeComment(basicComment);
            responseObject.put("result", result.name().toLowerCase());
            responseObject.put("commentCount", basicArticle.getCommentCount());
        }
        return responseObject.toString();
    }

    // aid 와 같은 article_index 찾기
    @GetMapping(value = "comment",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getComment(@SessionAttribute(value = "user", required = false) UserEntity user, @RequestParam(value = "aid") int articleIndex) {

        JSONArray responseArray = new JSONArray();
        BasicCommentVo[] comments = this.basicBbsService.getComments(articleIndex, user);

        for (BasicCommentVo comment : comments) {
            JSONObject commentObject = new JSONObject();
            commentObject.put("index", comment.getIndex());
            commentObject.put("userEmail", comment.getUserEmail());
            commentObject.put("userNickname", comment.getUserNickname());
            commentObject.put("articleIndex", comment.getArticleIndex());
            commentObject.put("content", comment.getContent());
            commentObject.put("writtenOn", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(comment.getWrittenOn()));
            // new SimpleDataFormat("형식").format([Date 타입 객체]) : [Date 타입 객체]가 가진 일시를 원하는 형식의 문자열로 만들어 버린다.
            commentObject.put("isSigned", user != null);    // 로그인이 되어 있으면 true
            commentObject.put("isMine", user != null && user.getEmail().equals(comment.getUserEmail())); // 로그인이 되어 있고, 그 유저가
            responseArray.put(commentObject);
        }
        return responseArray.toString();
    }

    // 댓글 삭제
//    @DeleteMapping(value = "comment",
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public String deleteComment(@SessionAttribute(value = "user", required = false) UserEntity user, BasicCommentVo comment) {
//
//        Enum<?> result = this.basicBbsService.deleteComment(comment, user);
//        JSONObject responseJson = new JSONObject();
//        responseJson.put("result", result.name().toLowerCase());
//
//        return responseJson.toString();
//    }
    @DeleteMapping(value = "comment",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteComment(@SessionAttribute(value = "user", required = false) UserEntity user,
                                @RequestParam(value = "index", required = false) int[] indexes) {

        int count = 0;
        for (int index : indexes) {
            BasicCommentVo basicComment = new BasicCommentVo();
            basicComment.setIndex(index);
            count += this.basicBbsService.deleteComment(basicComment, user) == CommonResult.SUCCESS ? 1 : 0;
        }
        Enum<?> result = count == indexes.length
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
        JSONObject responseJson = new JSONObject();
        responseJson.put("result", result.name().toLowerCase());
        return responseJson.toString();
    }

    // 댓글 수정
    @PatchMapping(value = "comment",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchComment(@SessionAttribute(value = "user", required = false) UserEntity user, BasicCommentVo comment) {

        Enum<?> result = this.basicBbsService.modifyComment(comment, user);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }

    // 게시글 삭제
    @DeleteMapping(value = "read",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteRead(@SessionAttribute(value = "user", required = false) UserEntity user,
                             @RequestParam(value = "aid") int[] aids) {
        String bid = null;
        int count = 0;
        for (int aid : aids) {
            System.out.println(aid);
            BasicArticleVo basicArticle = new BasicArticleVo();
            basicArticle.setIndex(aid);
            count += this.basicBbsService.deleteArticle(basicArticle, user) == CommonResult.SUCCESS ? 1 : 0;
            bid = basicArticle.getBoardId();
        }
        Enum<?> result = count == aids.length
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseObject.put("bid", bid);
        }
        return responseObject.toString();
    }

    // 글 수정하기
    @GetMapping(value = "modify",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getModify(@SessionAttribute(value = "user", required = false) UserEntity user, @RequestParam(value = "aid") int aid) {
        ModelAndView modelAndView = new ModelAndView("basicBbs/modify");
        BasicArticleVo basicArticle = new BasicArticleVo();
        basicArticle.setIndex(aid);
        Enum<?> result = this.basicBbsService.prepareModifyArticle(basicArticle, user);
        modelAndView.addObject("basicArticle", basicArticle);
        modelAndView.addObject("result", result.name());
        if (result == CommonResult.SUCCESS) {
            modelAndView.addObject("board", this.basicBbsService.getBoard(basicArticle.getBoardId()));
        }
        return modelAndView;
    }

    @PatchMapping(value = "modify",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchModify(@SessionAttribute(value = "user", required = false) UserEntity user, BasicArticleVo basicArticle,
                              @RequestParam(value = "aid") int aid) {
        basicArticle.setIndex(aid);
        Enum<?> result = this.basicBbsService.modifyArticle(basicArticle, user);
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseObject.put("aid", aid);
        }
        return responseObject.toString();
    }

    // 게시글 리스트
    @GetMapping(value = "list",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getList(@RequestParam(value = "bid") String bid,
                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                @RequestParam(value = "criterion", required = false) String criterion,
                                @RequestParam(value = "keyword", required = false) String keyword) {
        page = Math.max(1, page);
        ModelAndView modelAndView = new ModelAndView("basicBbs/list");
        BoardEntity board = this.basicBbsService.getBoard(bid);
        modelAndView.addObject("board", board);
        if (board != null) {
            int totalCount = this.basicBbsService.getArticleCount(board, criterion, keyword);

            PagingModel paging = new PagingModel(totalCount, page);
            modelAndView.addObject("paging", paging);

            BasicArticleVo[] basicArticles = this.basicBbsService.getArticles(board, paging, criterion, keyword);
            modelAndView.addObject("basicArticles", basicArticles);
        }
        return modelAndView;
    }

    // 게시글 좋아요
    @PostMapping(value = "basic-like",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postBasicLike(@SessionAttribute(value = "user", required = false) UserEntity user, BasicArticleLikeEntity basicArticleLike) {
        JSONObject responseJson = new JSONObject();
        Enum<?> result = this.basicBbsService.likeBasicArticle(basicArticleLike, user);
        BasicArticleVo basicArticle = this.basicBbsService.readArticle(basicArticleLike.getArticleIndex(), user);
        responseJson.put("result", result.name().toLowerCase());
        responseJson.put("isLiked", basicArticle.isLiked());
        responseJson.put("likeCount", basicArticle.getLikeCount());
        return responseJson.toString();
    }

    // 게시글 좋아요 취소
//    @DeleteMapping(value = "basic-like",
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public String deleteBasicLike(@SessionAttribute(value = "user", required = false) UserEntity user, BasicArticleLikeEntity basicArticleLike) {
//
//        JSONObject responseJson = new JSONObject();
//        Enum<?> result = this.basicBbsService.unlikeBasic(basicArticleLike, user);
//        BasicArticleVo basicArticle = this.basicBbsService.readArticle(basicArticleLike.getArticleIndex(), user);
//        responseJson.put("result", result.name().toLowerCase());
//        responseJson.put("isLiked", basicArticle.isLiked());
//        responseJson.put("likeCount", basicArticle.getLikeCount());
//        return responseJson.toString();
//    }
    @DeleteMapping(value = "basic-like",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteBasicLike(@SessionAttribute(value = "user", required = false) UserEntity user,
                                  @RequestParam(value = "articleIndex", required = false) int[] aids) {

        Boolean isLiked = null;
        int likeCount = 0;
        int count = 0;
        for (int aid : aids) {
            BasicArticleLikeEntity basicArticleLike = new BasicArticleLikeEntity();
            basicArticleLike.setArticleIndex(aid);
            count += this.basicBbsService.unlikeBasic(basicArticleLike, user) == CommonResult.SUCCESS ? 1 : 0;
            BasicArticleVo basicArticle = this.basicBbsService.readArticle(basicArticleLike.getArticleIndex(), user);
            isLiked = basicArticle.isLiked();
            likeCount = basicArticle.getLikeCount();
        }

        Enum<?> result = count == aids.length
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
        JSONObject responseJson = new JSONObject();
        responseJson.put("result", result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("isLiked", isLiked);
            responseJson.put("likeCount", likeCount);
        }
        return responseJson.toString();
    }


    // 게시글 신고하기
    @PostMapping(value = "article-report",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postArticleReport(
            @SessionAttribute(value = "user", required = false) UserEntity user,
            BasicArticleReportEntity basicArticleReport,
            @RequestParam(value = "aid", required = false) int aid) {
        JSONObject responseObject = new JSONObject();
        basicArticleReport.setArticleIndex(aid);
        Enum<?> result = this.basicBbsService.articleReport(basicArticleReport, user);
        BasicArticleVo basicArticle = this.basicBbsService.readArticle(basicArticleReport.getArticleIndex(), user);

        responseObject.put("result", result.name().toLowerCase());
        responseObject.put("isReported", basicArticle.isReported());
        return responseObject.toString();
    }

    // 신고 초기화
    @DeleteMapping(value = "article-report",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteArticleReport(
            @SessionAttribute(value = "user", required = false) UserEntity user,
            @RequestParam(value = "aid", required = false) int[] aids) {
        int count = 0;
        for (int aid : aids) {
            BasicArticleReportEntity basicArticleReport = new BasicArticleReportEntity();
            basicArticleReport.setArticleIndex(aid);
            count += this.basicBbsService.resetReport(basicArticleReport, user) == CommonResult.SUCCESS ? 1 : 0;
        }
        Enum<?> result = count == aids.length ? CommonResult.SUCCESS : CommonResult.FAILURE;
        JSONObject responseObject = new JSONObject();
        responseObject.put("result", result.name().toLowerCase());
        return responseObject.toString();
    }

    // 관리자 페이지
    @GetMapping(value = "basic-admin",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getBasicAdmin(
            @RequestParam(value = "bid") String bid,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "criterion", required = false) String criterion,
            @RequestParam(value = "keyword", required = false) String keyword,
            @SessionAttribute(value = "user", required = false) UserEntity user,
            HttpSession session) {
        page = Math.max(1, page);
        ModelAndView modelAndView = new ModelAndView("basicBbs/admin");
        if (user != null) {
            session.setAttribute("userEmail", user.getEmail());
        }
        BoardEntity board = this.basicBbsService.getBoard(bid);
        modelAndView.addObject("board", board);
        if (board != null) {
            int totalCount = this.basicBbsService.getReportedArticleCount(board, criterion, keyword);

            PagingModel paging = new PagingModel(totalCount, page);
            modelAndView.addObject("paging", paging);

            BasicArticleVo[] reportedArticles = this.basicBbsService.getReportedArticles(board, paging, criterion, keyword);
            modelAndView.addObject("reportedArticles", reportedArticles);
        }
        return modelAndView;
    }

}
