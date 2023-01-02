package com.emptybeer.etb.mappers;

import com.emptybeer.etb.entities.data.BeerEntity;
import com.emptybeer.etb.vos.BeerVo;
import com.emptybeer.etb.vos.ReviewArticleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IDataMapper {
    BeerVo selectBeerByIndex(@Param(value = "beerIndex") int beerIndex);

    BeerVo[] selectBeer();
}
