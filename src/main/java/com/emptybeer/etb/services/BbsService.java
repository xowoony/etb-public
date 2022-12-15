package com.emptybeer.etb.services;

import com.emptybeer.etb.entities.bbs.BoardEntity;
import com.emptybeer.etb.entities.bbs.ReviewArticleEntity;
import com.emptybeer.etb.entities.data.BeerEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.enums.bbs.WriteResult;
import com.emptybeer.etb.interfaces.IResult;
import com.emptybeer.etb.mappers.IBbsMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service(value = "com.emptybeer.etb.services.BbsServices")
public class BbsService {
    private final IBbsMapper bbsMapper;

    @Autowired
    public BbsService(IBbsMapper bbsMapper) {
        this.bbsMapper = bbsMapper;
    }

    public BoardEntity getBoard(String id) {
        return this.bbsMapper.selectBoardById(id);
    }

    public BeerEntity getBeer(int beerIndex) {
        return this.bbsMapper.selectBeerByIndex(beerIndex);
    }

    public Enum<? extends IResult> reviewAdd(ReviewArticleEntity reviewArticle) {
        return this.bbsMapper.insertReviewArticle(reviewArticle) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }
}
