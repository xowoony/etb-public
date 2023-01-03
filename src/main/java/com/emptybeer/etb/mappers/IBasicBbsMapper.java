package com.emptybeer.etb.mappers;

import com.emptybeer.etb.entities.bbs.BasicArticleEntity;
import com.emptybeer.etb.entities.bbs.BoardEntity;
import com.emptybeer.etb.entities.bbs.ImageEntity;
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
}
