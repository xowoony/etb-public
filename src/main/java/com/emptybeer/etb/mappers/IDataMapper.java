package com.emptybeer.etb.mappers;

import com.emptybeer.etb.entities.data.BeerEntity;
import com.emptybeer.etb.vos.BeerVo;
import com.emptybeer.etb.vos.ReviewArticleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IDataMapper {
    // 맥주 이미지, 이름 가져오기
    BeerVo selectBeerByIndex(@Param(value = "beerIndex") int beerIndex);

    // 맥주 이미지, 이름정보 배열로 가져오기
    BeerVo[] selectBeer(@Param(value = "criterion") String criterion,
                        @Param(value = "keyword") String keyword,
                        @Param(value = "limit") int limit,
                        @Param(value = "offset") int offset,
                        // 맥주 카테고리별 보기
                        @Param(value = "beerCategory") String beerCategory);


    // 페이징
    int selectBeerCountByBeerIndex(@Param(value = "criterion") String criterion,
                                   @Param(value = "keyword") String keyword,
                                   @Param(value = "beerCategory") String beerCategory);

    int selectBeerCount();

    // 인기 맥주 배열로 가져오기
    BeerVo[] selectBeerRanking();


    //관리자
    BeerVo[] selectBeerForAdmin(@Param(value = "limit") int limit,
                                @Param(value = "offset") int offset);

    int insertBeer(BeerEntity beer);

    BeerEntity selectBeerFromAdminByIndex(@Param(value = "index") int index);

    int updateBeer(BeerEntity beer);

    int updateBeerExceptImage(BeerEntity beer);

    int deleteBeerByIndex(@Param(value = "index") int index);

}
