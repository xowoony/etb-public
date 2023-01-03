package com.emptybeer.etb.mappers;

import com.emptybeer.etb.entities.bbs.BasicArticleEntity;
import com.emptybeer.etb.entities.bbs.BoardEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IBasicBbsMapper {
    BoardEntity selectBoardById(@Param(value = "id") String id);

    // 글 쓰기
    int insertArticle(BasicArticleEntity basicArticle);
}
