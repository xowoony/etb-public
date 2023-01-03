package com.emptybeer.etb.mappers;

import com.emptybeer.etb.entities.bbs.FestivalArticleEntity;
import com.emptybeer.etb.entities.bbs.FestivalCommentEntity;
import com.emptybeer.etb.entities.bbs.ImageEntity;
import com.emptybeer.etb.vos.FestivalCommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IFestivalMapper {
    // festival관련
    FestivalArticleEntity[] selectFestivalArticle();

    FestivalArticleEntity selectFestivalArticleByIndex(@Param(value="index")int index);

    FestivalCommentVo[] selectFestivalCommentByArticleIndex(@Param(value="index")int index);

    ImageEntity selectImageByIndex(@Param(value="index") int index);

    int insertFestivalComment(FestivalCommentEntity festivalComment);

}
