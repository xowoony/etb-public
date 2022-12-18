package com.emptybeer.etb.mappers;

import com.emptybeer.etb.entities.bbs.*;
import com.emptybeer.etb.entities.data.BeerEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IBbsMapper {
    BoardEntity selectBoardById(@Param(value = "id") String id);

    BeerEntity selectBeerByIndex(@Param(value = "beerIndex") int beerIndex);

    int insertReviewArticle(ReviewArticleEntity reviewArticle);

    int selectReviewByBeerIndex(@Param(value = "userEmail") String userEmail,
                                @Param(value = "beerIndex") int beerIndex);
}
