package com.emptybeer.etb.mappers;

import com.emptybeer.etb.entities.bbs.BasicArticleEntity;
import com.emptybeer.etb.entities.bbs.BasicCommentEntity;
import com.emptybeer.etb.entities.bbs.BoardEntity;
import com.emptybeer.etb.entities.bbs.ImageEntity;
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
    BasicArticleVo selectIndex(@Param(value = "index") int index);

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
}
