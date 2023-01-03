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


    BeerVo[] selectBeer(@Param(value = "criterion") String criterion,
                        @Param(value = "keyword") String keyword,
                        @Param(value = "limit") int limit,
                        @Param(value = "offset") int offset);

    // 페이징
    int selectBeerCountByBeerIndex(@Param(value = "criterion") String criterion,
                                   @Param(value = "keyword") String keyword);
}
