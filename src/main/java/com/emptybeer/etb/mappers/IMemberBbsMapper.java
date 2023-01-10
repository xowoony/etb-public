package com.emptybeer.etb.mappers;

import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.vos.BasicArticleVo;
import com.emptybeer.etb.vos.ReviewArticleVo;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMemberBbsMapper {

    // 작성글 리스트
    BasicArticleVo[] selectArticlesByEmail(@Param(value = "email") String email);

    // 작성 리뷰 리스트
    ReviewArticleVo[] selectReviewsByEmail(@Param(value = "email") String email);
}
