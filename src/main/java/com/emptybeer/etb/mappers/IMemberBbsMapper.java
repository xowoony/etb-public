package com.emptybeer.etb.mappers;

import com.emptybeer.etb.entities.bbs.BasicArticleLikeEntity;
import com.emptybeer.etb.entities.bbs.ReviewArticleLikeEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.vos.*;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMemberBbsMapper {

    // 작성글 리스트
    BasicArticleVo[] selectArticlesByEmail(@Param(value = "email") String email);

    // 작성 리뷰 리스트
    ReviewArticleVo[] selectReviewsByEmail(@Param(value = "email") String email);

    // 작성 댓글 리스트
    BasicCommentVo[] selectCommentsByEmail(@Param(value = "email") String email);

    // 좋아요 한 기본 게시글 리스트
    BasicArticleLikeVo[] selectArticlesByLike(@Param(value = "email") String email);

    // 좋아요한 리뷰 게시글 리스트
    ReviewArticleLikeVo[] selectReviewsByLike(@Param(value = "email") String email);
}
