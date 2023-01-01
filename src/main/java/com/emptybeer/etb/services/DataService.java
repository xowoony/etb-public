package com.emptybeer.etb.services;

import com.emptybeer.etb.entities.data.BeerEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.mappers.IDataMapper;
import com.emptybeer.etb.models.PagingModel;
import com.emptybeer.etb.vos.BeerVo;
import com.emptybeer.etb.vos.ReviewArticleVo;
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
//    public BeerEntity getBeer(int index) {
//        return this.dataMapper.selectBeerImageByIndex(index);
//    }



}
