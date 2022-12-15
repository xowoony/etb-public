package com.emptybeer.etb.mappers;

import com.emptybeer.etb.entities.bbs.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IBbsMapper {
    BoardEntity selectBoardById(@Param(value = "id") String id);

    int insertReviewArticle(ReviewArticleEntity reviewArticle);
}
