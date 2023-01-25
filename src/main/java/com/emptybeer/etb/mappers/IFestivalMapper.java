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
    FestivalArticleEntity[] selectFestivalArticle(@Param(value="limit")int limit,
                                                  @Param(value="offset")int offset);


    FestivalArticleEntity selectFestivalArticleByIndex(@Param(value="index")int index);


    int selectFestivalArticleForCount();

    FestivalCommentVo[] selectFestivalCommentByArticleIndex(@Param(value="index")int index,
                                                            @Param(value = "limit") int limit,
                                                            @Param(value = "offset") int offset);
    FestivalArticleEntity[] selectFestivalArticleForPaging(@Param(value="limit")int limit,
                                                           @Param(value="offset")int offset);

    ImageEntity selectImageByIndex(@Param(value="index") int index);

    int insertFestivalComment(FestivalCommentEntity festivalComment);


    FestivalCommentEntity selectFestivalCommentByCommentIndex(@Param(value="index")int index);

    int deleteFestivalCommentByIndex(@Param(value="index") int index);


    int updateFestivalCommentByIndex(@Param(value="content")String content, @Param(value="index")int index);


    int updateFestivalCommentByIndexFromFestivalModify(@Param(value="index")int index, @Param(value="content")String content);

    int selectFestivalCommentCountByFestivalArticleIndex(@Param(value="index")int index);

    FestivalCommentEntity selectFestivalArticleIndexByIndexFromFestivalModify(@Param(value="index")int index);




    //festivalAdmin 관련
    int insertFestivalArticle(FestivalArticleEntity festivalArticle);


    int updateFestivalArticle(FestivalArticleEntity festivalArticle);

    int updateFestivalArticleExceptImage(FestivalArticleEntity festivalArticle);

    int deleteFestivalArticleByIndex(@Param(value="index")int index);
}
