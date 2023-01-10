package com.emptybeer.etb.services;

import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.mappers.IMemberBbsMapper;
import com.emptybeer.etb.vos.BasicArticleVo;
import com.emptybeer.etb.vos.ReviewArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "package com.emptybeer.etb.services.MemberBbsService")
public class MemberBbsService {
    private final IMemberBbsMapper memberBbsMapper;

    @Autowired
    public MemberBbsService(IMemberBbsMapper memberBbsMapper) {
        this.memberBbsMapper = memberBbsMapper;
    }

    // 작성글 리스트
    public BasicArticleVo[] getMyArticles(UserEntity user) {
        return this.memberBbsMapper.selectArticlesByEmail(user.getEmail());
    }

    // 작성 리뷰 리스트
    public ReviewArticleVo[] getMyReviews(UserEntity user) {
        return this.memberBbsMapper.selectReviewsByEmail(user.getEmail());
    }

}
