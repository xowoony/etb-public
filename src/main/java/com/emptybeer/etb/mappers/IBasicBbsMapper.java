package com.emptybeer.etb.mappers;

import com.emptybeer.etb.entities.bbs.*;
import com.emptybeer.etb.vos.BasicArticleVo;
import com.emptybeer.etb.vos.BasicCommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IBasicBbsMapper {
    BoardEntity selectBoardById(@Param(value = "id") String id);

    // 글 쓰기
    int insertArticle(BasicArticleEntity basicArticle);

    // 이미지
    int insertImage(ImageEntity image);

    // 이미지 관련
    ImageEntity selectImageByIndex(@Param(value = "index") int index);

    // 글 읽기
    BasicArticleVo selectIndex(
            @Param(value = "index") int index);

    // 글 읽기(추천 포함)
    BasicArticleVo selectIndexUser(
            @Param(value = "index") int index,
            @Param(value = "userEmail") String userEmail);

    // 게시글 수정
    int updateArticle(BasicArticleVo basicArticle);

    // 댓글 입력
    int insertComment(BasicCommentEntity basicComment);

    // 댓글
    BasicCommentVo[] selectCommentsByArticleIndex(@Param(value = "articleIndex") int articleIndex,
                                                  @Param(value = "userEmail") String userEmail);

    // 댓글 삭제
    BasicCommentVo selectCommentByIndex(@Param(value = "index") int index);

    int deleteCommentByIndex(@Param(value = "index") int index);
    // index 값을 기준으로 삭제하는 이유는 index 값이 pk이기 때문에

    // 댓글 수정
    int modifyCommentByIndex(BasicCommentVo comment);

    // 게시글 삭제
    int deleteArticleByIndex(@Param(value = "index") int index);

    // 게시글 리스트 count
    int selectArticleCountByBoardId(@Param(value = "boardId") String boardId,
                                    @Param(value = "criterion") String criterion,
                                    @Param(value = "keyword") String keyword);

    // 게시글 리스트
    BasicArticleVo[] selectArticlesByBoardId(@Param(value = "boardId") String boardId,
                                             @Param(value = "criterion") String criterion,
                                             @Param(value = "keyword") String keyword,
                                             @Param(value = "limit") int limit,
                                             @Param(value = "offset") int offset);

    // 게시글 좋아요
    int insertBasicArticleLike(BasicArticleLikeEntity basicArticleLike);

    // 게시글 좋아요 취소
    int deleteBasicLike(BasicArticleLikeEntity basicArticleLike);

    // 게시글 신고하기
    int insertArticleReport(BasicArticleReportEntity basicArticleReport);

    // 신고 게시물 리스트
    BasicArticleVo[] selectReportedArticlesByBoardId(@Param(value = "boardId") String boardId,
                                             @Param(value = "criterion") String criterion,
                                             @Param(value = "keyword") String keyword,
                                             @Param(value = "limit") int limit,
                                             @Param(value = "offset") int offset);

    // 신고 게시글 리스트 count
    int selectReportedArticleCountByBoardId(@Param(value = "boardId") String boardId,
                                    @Param(value = "criterion") String criterion,
                                    @Param(value = "keyword") String keyword);


    // 체크 게시글
    BasicArticleVo[] selectCheckedArticlesByIndex(@Param(value = "index") int index);


    // 신고 수 리셋
    int deleteReportByArticleIndex(@Param(value = "articleIndex") int index);


}
