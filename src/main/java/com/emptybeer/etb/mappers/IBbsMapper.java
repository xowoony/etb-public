package com.emptybeer.etb.mappers;

import com.emptybeer.etb.entities.bbs.*;
import com.emptybeer.etb.entities.data.BeerEntity;
import com.emptybeer.etb.entities.data.BeerLikeEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.vos.BeerVo;
import com.emptybeer.etb.vos.ReviewArticleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IBbsMapper {
    BoardEntity selectBoardById(@Param(value = "id") String id);

    BeerVo selectBeerByIndex(@Param(value = "beerIndex") int beerIndex);

    BeerVo selectBeerLikeByIndex(@Param(value = "userEmail") String userEmail,
                                 @Param(value = "beerIndex") int beerIndex);

    // 리뷰 등록
    int insertReviewArticle(ReviewArticleEntity reviewArticle);

    // 개인이 적은 맥주당 리뷰 개수 확인(중복 등록 방지)
    int selectReviewByBeerIndex(@Param(value = "userEmail") String userEmail,
                                @Param(value = "beerIndex") int beerIndex);

    int selectReviewArticleCountByBeerIndex(@Param(value = "beerIndex") int beerIndex,
                                            @Param(value = "criterion") String criterion,
                                            @Param(value = "keyword") String keyword);

    // 맥주 점수 평균
    double selectReviewAvgByBeerIndex(@Param(value = "beerIndex") int beerIndex);

    ReviewArticleVo[] selectReviewArticleByBeerIndex(@Param(value = "beerIndex") int beerIndex,
                                                     @Param(value = "criterion") String criterion,
                                                     @Param(value = "keyword") String keyword,
                                                     @Param(value = "limit") int limit,
                                                     @Param(value = "offset") int offset);

    int insertBeerLike(BeerLikeEntity beerLike);

    int deleteBeerLike(BeerLikeEntity beerLike);

    ReviewArticleVo selectLikeIndex(@Param(value = "userEmail") String userEmail,
                                    @Param(value = "index") int index);

    // 리뷰 수정
    int updateReview(ReviewArticleVo reviewArticle);

    // 리뷰 읽기
    ReviewArticleVo selectIndex(@Param(value = "index") int index);

    // 리뷰 삭제
    int deleteReviewByIndex(@Param(value = "index") int index);


    // festival관련
    FestivalArticleEntity[] selectFestivalArticle();

    ImageEntity selectImageByIndex(@Param(value="index") int index);

}
