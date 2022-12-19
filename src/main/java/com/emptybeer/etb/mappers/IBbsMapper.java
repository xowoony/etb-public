package com.emptybeer.etb.mappers;

import com.emptybeer.etb.entities.bbs.*;
import com.emptybeer.etb.entities.data.BeerEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.vos.ReviewArticleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IBbsMapper {
    BoardEntity selectBoardById(@Param(value = "id") String id);

    BeerEntity selectBeerByIndex(@Param(value = "beerIndex") int beerIndex);

    int insertReviewArticle(ReviewArticleEntity reviewArticle);

    int selectReviewByBeerIndex(@Param(value = "userEmail") String userEmail,
                                @Param(value = "beerIndex") int beerIndex);

    int selectReviewArticleCountByBeerIndex(@Param(value = "beerIndex") int beerIndex,
                                    @Param(value = "criterion") String criterion,
                                    @Param(value = "keyword") String keyword);

    ReviewArticleVo[] selectReviewArticleByBeerIndex(@Param(value = "beerIndex") int beerIndex,
                                                     @Param(value = "criterion") String criterion,
                                                     @Param(value = "keyword") String keyword,
                                                     @Param(value = "limit") int limit,
                                                     @Param(value = "offset") int offset);
}
