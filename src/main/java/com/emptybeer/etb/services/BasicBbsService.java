package com.emptybeer.etb.services;

import com.emptybeer.etb.entities.bbs.*;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.enums.basicBbs.CommentResult;
import com.emptybeer.etb.enums.bbs.ArticleModifyResult;
import com.emptybeer.etb.enums.bbs.WriteResult;
import com.emptybeer.etb.interfaces.IResult;
import com.emptybeer.etb.mappers.IBasicBbsMapper;
import com.emptybeer.etb.models.PagingModel;
import com.emptybeer.etb.vos.BasicArticleVo;
import com.emptybeer.etb.vos.BasicCommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

@Service(value = "com.emptybeer.etb.services.BasicBbsService")
public class BasicBbsService {
    private final IBasicBbsMapper basicBbsMapper;

    @Autowired
    public BasicBbsService(IBasicBbsMapper basicBbsMapper) {
        this.basicBbsMapper = basicBbsMapper;
    }

    public BoardEntity getBoard(String id) {
        return this.basicBbsMapper.selectBoardById(id);
    }

    // 글 적기
    public Enum<? extends IResult> write(BasicArticleEntity basicArticle) {
        BoardEntity board = this.basicBbsMapper.selectBoardById(basicArticle.getBoardId());

        if (board == null) {
            return WriteResult.NO_SUCH_BOARD;
        }
        return this.basicBbsMapper.insertArticle(basicArticle) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    // 이미지
    public Enum<? extends IResult> addImage(ImageEntity image) {
        return this.basicBbsMapper.insertImage(image) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    // 이미지 넣는 기
    public ImageEntity getImage(int index) {
        return this.basicBbsMapper.selectImageByIndex(index);
    }

    // 글 읽기 + 닉네임 불러오기
    public BasicArticleVo readArticle(int index, UserEntity signedUser) {
        BasicArticleVo basicArticle = this.basicBbsMapper.selectIndexUser(index, signedUser == null ? null : signedUser.getEmail());
        basicArticle.setIndex(this.basicBbsMapper.updateArticle(basicArticle));
        return this.basicBbsMapper.selectIndexUser(index, signedUser == null ? null : signedUser.getEmail());
    }

    // 댓글 작성
    public Enum<? extends IResult> writeComment(BasicCommentEntity basicComment) {
        BasicArticleVo basicArticle = this.basicBbsMapper.selectIndex(basicComment.getArticleIndex());

        if (basicArticle == null) {      // 게시글이 없으면
            return CommonResult.FAILURE;
        }

        return this.basicBbsMapper.insertComment(basicComment) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    // aid 와 같은 article_index 찾기
    public BasicCommentVo[] getComments(int articleIndex, UserEntity signedUser) {

        return this.basicBbsMapper.selectCommentsByArticleIndex
                (articleIndex, signedUser == null ? null : signedUser.getEmail());
    }

    // 댓글 삭제
    public Enum<? extends IResult> deleteComment(BasicCommentVo comment, UserEntity user) {
        BasicCommentVo existingComment = this.basicBbsMapper.selectCommentByIndex(comment.getIndex());

        // 지금 삭제하려는 댓글이 존재하지 않을 경우
        if (existingComment == null) {
            return CommentResult.NO_SUCH_COMMENT;
        }

        // 여기까지 내려오면 댓글이 존재는 함
        // 로그인이 안 되었고 + 삭제하려는 댓글이 본인 댓글이 아닌 경우 (보안조치)
        // 프론트는 믿으면 안 된다, 백엔드에서 해결해야함
        if (user == null || !user.getEmail().equals(existingComment.getUserEmail())) {
            return CommentResult.NOT_ALLOWED;
        }

        // 실패 : deleteComment 호출 결과가 0
        // 성공
        return this.basicBbsMapper.deleteCommentByIndex(comment.getIndex()) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    // 댓글 수정
    public Enum<? extends IResult> modifyComment(BasicCommentVo comment, UserEntity user) {

        BasicCommentVo existingComment = this.basicBbsMapper.selectCommentByIndex(comment.getIndex());

        if (existingComment == null) {
            return CommentResult.NO_SUCH_COMMENT;
        }

        if (user == null || !user.getEmail().equals(existingComment.getUserEmail())) {
            return CommentResult.NOT_ALLOWED;
        }
        existingComment.setContent(comment.getContent());

        return this.basicBbsMapper.modifyCommentByIndex(existingComment) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    // 게시글 삭제
    public Enum<? extends IResult> deleteArticle(BasicArticleVo basicArticle, UserEntity user) {
        BasicArticleVo existingArticle = this.basicBbsMapper.selectIndex(basicArticle.getIndex());
        if (existingArticle == null) {
            return CommonResult.FAILURE;
        }
        if (user == null || !(user.getEmail().equals(existingArticle.getUserEmail())|| user.getEmail().equals("admin@admin"))) {
            return CommonResult.FAILURE;
        }

        basicArticle.setBoardId(existingArticle.getBoardId());

        return this.basicBbsMapper.deleteArticleByIndex(basicArticle.getIndex()) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    // 게시글 수정
    public Enum<? extends IResult> prepareModifyArticle(BasicArticleVo basicArticle, UserEntity user) {

        if (user == null) {
            return ArticleModifyResult.NOT_SIGNED;
        }
        BasicArticleVo existingArticle = this.basicBbsMapper.selectIndex(basicArticle.getIndex());

        if (existingArticle == null) {
            return ArticleModifyResult.NO_SUCH_ARTICLE;
        }

        if (!user.getEmail().equals(existingArticle.getUserEmail())) {
            return ArticleModifyResult.NOT_ALLOWED;
        }
        // POST일 경우만 담아주면 된다?
        basicArticle.setIndex(existingArticle.getIndex());
        basicArticle.setUserEmail(existingArticle.getUserEmail());
        basicArticle.setBoardId(existingArticle.getBoardId());
        basicArticle.setTitle(existingArticle.getTitle());
        basicArticle.setContent(existingArticle.getContent());
        basicArticle.setView(existingArticle.getView());
        basicArticle.setWrittenOn(existingArticle.getWrittenOn());
        basicArticle.setModifiedOn(existingArticle.getModifiedOn());
        return CommonResult.SUCCESS;
    }

    public Enum<? extends IResult> modifyArticle(BasicArticleVo basicArticle, UserEntity user) {
        if (user == null) {
            return ArticleModifyResult.NOT_SIGNED;
        }
        BasicArticleVo existingArticle = this.basicBbsMapper.selectIndex(basicArticle.getIndex());
        if (existingArticle == null) {
            return ArticleModifyResult.NO_SUCH_ARTICLE;
        }
        if (!existingArticle.getUserEmail().equals(user.getEmail())) {
            return ArticleModifyResult.NOT_ALLOWED;
        }

        existingArticle.setTitle(basicArticle.getTitle());
        existingArticle.setContent(basicArticle.getContent());
        existingArticle.setModifiedOn(new Date());
        return this.basicBbsMapper.updateArticle(existingArticle) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }


    // 게시글 리스트 카운트
    public int getArticleCount(BoardEntity board, String criterion, String keyword) {
        return this.basicBbsMapper.selectArticleCountByBoardId(board.getId(), criterion, keyword);
    }

    // 게시글 리스트
    public BasicArticleVo[] getArticles(BoardEntity board, PagingModel paging, String criterion, String keyword) {
        return this.basicBbsMapper.selectArticlesByBoardId(
                board.getId(), criterion, keyword,
                paging.countPerPage,
                (paging.requestPage - 1) * paging.countPerPage);
    }


    // 게시글 좋아요
    public Enum<? extends IResult> likeBasicArticle(BasicArticleLikeEntity basicArticleLike, UserEntity user) {
        if (user == null) {
            return CommonResult.FAILURE;
        }
        basicArticleLike.setUserEmail(user.getEmail());
        return this.basicBbsMapper.insertBasicArticleLike(basicArticleLike) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    // 게시글 좋아요 취소
    public Enum<? extends IResult> unlikeBasic(BasicArticleLikeEntity basicArticleLike, UserEntity user) {
        if (user == null) {
            return CommonResult.FAILURE;
        }
        basicArticleLike.setUserEmail(user.getEmail());
        return this.basicBbsMapper.deleteBasicLike(basicArticleLike) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;

    }

    // 게시글 신고
    public Enum<? extends IResult> articleReport(BasicArticleReportEntity basicArticleReport, UserEntity user) {
        if (user == null) {
            return CommonResult.FAILURE;
        }
        basicArticleReport.setUserEmail(user.getEmail());
        return this.basicBbsMapper.insertArticleReport(basicArticleReport) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    // 신고 게시물 리스트
    public BasicArticleVo[] getReportedArticles(BoardEntity board, PagingModel paging, String criterion, String keyword) {
        return this.basicBbsMapper.selectReportedArticlesByBoardId(
                board.getId(), criterion, keyword,
                paging.countPerPage,
                (paging.requestPage - 1) * paging.countPerPage);
    }

    // 신고 게시글 리스트 카운트
    public int getReportedArticleCount(BoardEntity board, String criterion, String keyword) {
        return this.basicBbsMapper.selectReportedArticleCountByBoardId(board.getId(), criterion, keyword);
    }

    // 체크 게시글
    public BasicArticleVo[] getCheckedArticles(int aid) {
        BasicArticleVo[] checkedArticles = this.basicBbsMapper.selectCheckedArticlesByIndex(aid);
        return checkedArticles;
    }

    // 신고 수 초기화
    public Enum<? extends IResult> resetReport(BasicArticleReportEntity basicArticleReport, UserEntity user) {

        if (user == null || !(user.getEmail().equals("admin@admin"))) {
            return CommonResult.FAILURE;
        }

        return this.basicBbsMapper.deleteReportByArticleIndex(basicArticleReport.getArticleIndex()) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

}
