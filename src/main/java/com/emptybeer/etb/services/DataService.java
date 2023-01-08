package com.emptybeer.etb.services;

import com.emptybeer.etb.mappers.IDataMapper;
import com.emptybeer.etb.models.PagingModel;
import com.emptybeer.etb.vos.BeerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service(value = "com.emptybeer.etb.services.DataService")
public class DataService {

    private final IDataMapper dataMapper;

    @Autowired
    public DataService(IDataMapper dataMapper) {
        this.dataMapper = dataMapper;
    }

    // 맥주 이미지, 이름 배열로 가져오기 + 맥주 카테고리별 보기
    public BeerVo[] getBeer(PagingModel paging, String criterion, String keyword, String beerCategory) {
        return this.dataMapper.selectBeer(criterion, keyword, paging.countPerPage,
                (paging.requestPage - 1) * paging.countPerPage, beerCategory);
    }


    // 맥주 이미지 가져오기
    public BeerVo getBeerImage(int beerIndex) {
        return this.dataMapper.selectBeerByIndex(beerIndex);
    }

    // 페이징(맥주 total count)
    public int getBeerCount(String criterion, String keyword) {
        return this.dataMapper.selectBeerCountByBeerIndex(criterion, keyword);
    }

    // 맥주 카테고리별 보기
//    public BeerVo getBeerCategory(int categoryIndex) {
//        return this.dataMapper.selectBeerCategory(categoryIndex);
//    }

    // 맥주 인기 이미지순 가져오기
//    public BeerVo getReviewRankingImage() {
//        return this.dataMapper.selectBeerRanking();
//    }

    // 맥주 인기순위 배열
    public BeerVo[] getBeerRanking() {
        return this.dataMapper.selectBeerRanking();
    }


}




