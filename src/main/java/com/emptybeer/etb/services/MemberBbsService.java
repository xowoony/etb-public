package com.emptybeer.etb.services;

import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.mappers.IMemberBbsMapper;
import com.emptybeer.etb.mappers.IMemberMapper;
import com.emptybeer.etb.vos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "package com.emptybeer.etb.services.MemberBbsService")
public class MemberBbsService {
    private final IMemberBbsMapper memberBbsMapper;
    private final IMemberMapper memberMapper;

    @Autowired
    public MemberBbsService(IMemberBbsMapper memberBbsMapper, IMemberMapper memberMapper) {
        this.memberBbsMapper = memberBbsMapper;
        this.memberMapper = memberMapper;
    }

    // 작성글 리스트
    public BasicArticleVo[] getMyArticles(UserEntity user) {
        return this.memberBbsMapper.selectArticlesByEmail(user.getEmail());
    }

    // 작성 리뷰 리스트
    public ReviewArticleVo[] getMyReviews(UserEntity user) {
        return this.memberBbsMapper.selectReviewsByEmail(user.getEmail());
    }

    // 작성 댓글 리스트
    public BasicCommentVo[] getMyComments(UserEntity user) {
        return this.memberBbsMapper.selectCommentsByEmail(user.getEmail());
    }

    // 좋아요한 기본 게시글 리스트
    public BasicArticleLikeVo[] getMyLikeArticles(UserEntity user) {
        return this.memberBbsMapper.selectArticlesByLike(user.getEmail());
    }

    // 좋아요한 리뷰 게시글 리스트
    public ReviewArticleLikeVo[] getMyLikeReviews(UserEntity user) {
        return this.memberBbsMapper.selectReviewsByLike(user.getEmail());
    }

}
