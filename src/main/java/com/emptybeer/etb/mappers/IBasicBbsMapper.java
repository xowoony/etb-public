package com.emptybeer.etb.mappers;

import com.emptybeer.etb.entities.bbs.BasicArticleEntity;
import com.emptybeer.etb.entities.bbs.BoardEntity;
import com.emptybeer.etb.entities.bbs.ImageEntity;
import com.emptybeer.etb.vos.BasicArticleVo;
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
}
