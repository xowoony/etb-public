package com.emptybeer.etb.services;

import com.emptybeer.etb.entities.data.BeerEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.interfaces.IResult;
import com.emptybeer.etb.mappers.IDataMapper;
import com.emptybeer.etb.models.PagingModel;
import com.emptybeer.etb.models.PagingModelBeer;
import com.emptybeer.etb.models.PagingModelFestival;
import com.emptybeer.etb.vos.BeerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Service(value = "com.emptybeer.etb.services.DataService")
public class DataService {

    private final IDataMapper dataMapper;

    @Autowired
    public DataService(IDataMapper dataMapper) {
        this.dataMapper = dataMapper;
    }

    // 맥주 이미지, 이름 배열로 가져오기 + 맥주 카테고리별 보기
    public BeerVo[] getBeer(PagingModelBeer paging, String criterion, String keyword, String beerCategory) {
        return this.dataMapper.selectBeer(criterion, keyword, paging.countPerPage,
                (paging.requestPage - 1) * paging.countPerPage, beerCategory);
    }


    // 맥주 이미지 가져오기
    public BeerVo getBeerImage(int beerIndex) {
        return this.dataMapper.selectBeerByIndex(beerIndex);
    }

    // 페이징(맥주 total count)
    public int getBeerCount(String criterion, String keyword, String beerCategory) {
        return this.dataMapper.selectBeerCountByBeerIndex(criterion, keyword, beerCategory);
    }

    // 맥주 카테고리별 보기
//    public BeerVo getBeerCategory(int categoryIndex) {
//        return this.dataMapper.selectBeerCategory(categoryIndex);
//    }

    // 맥주 인기순위 배열
    public BeerVo[] getBeerRanking() {
        return this.dataMapper.selectBeerRanking();
    }


    public int getBeerForPageCount(){ return this.dataMapper.selectBeerCount();}

    // 관리자

    public BeerVo[] getBeerForAdmin(PagingModelBeer paging){
        return this.dataMapper.selectBeerForAdmin(paging.countPerPage, (paging.requestPage - 1)* paging.countPerPage);
    }

    public Enum<? extends IResult> beerWrite(BeerEntity beer){
        return this.dataMapper.insertBeer(beer) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public BeerEntity getBeerByIndex(int index){
        return this.dataMapper.selectBeerFromAdminByIndex(index);
    }

    public Enum<? extends IResult> beerUpdate(BeerEntity beer, MultipartFile image) throws IOException {

        int result;
        //이미지가 삽입 되었을 시와 아닐시에 대한 로직
        if(image != null){
            beer.setImage(image.getBytes());
            result = this.dataMapper.updateBeer(beer);
        } else{
            result = this.dataMapper.updateBeerExceptImage(beer);
        }

        return result>0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public Enum<? extends IResult> deleteBeer(BeerEntity beer, UserEntity user){
        if(user==null){
            return CommonResult.FAILURE;
        } else {
            return this.dataMapper.deleteBeerByIndex(beer.getIndex()) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
        }

    }
}




