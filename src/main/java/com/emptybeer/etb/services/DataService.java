package com.emptybeer.etb.services;

import com.emptybeer.etb.mappers.IDataMapper;
import com.emptybeer.etb.vos.BeerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "com.emptybeer.etb.services.DataService")
public class DataService {

    private final IDataMapper dataMapper;

    @Autowired
    public DataService(IDataMapper dataMapper) {
        this.dataMapper = dataMapper;
    }

    // 맥주 이미지, 이름 가져오기
//    public BeerVo getBeer(int beerIndex) {
//        return this.dataMapper.selectBeerByIndex(beerIndex);
//    }

    public BeerVo[] getBeer() {
        return this.dataMapper.selectBeer();
    }


    public BeerVo getBeerImage(int beerIndex) {
        return this.dataMapper.selectBeerByIndex(beerIndex);
    }

}




