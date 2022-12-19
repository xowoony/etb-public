package com.emptybeer.etb.services;

import com.emptybeer.etb.entities.bbs.BoardEntity;
import com.emptybeer.etb.entities.bbs.ReviewArticleEntity;
import com.emptybeer.etb.entities.data.BeerEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.enums.bbs.WriteResult;
import com.emptybeer.etb.interfaces.IResult;
import com.emptybeer.etb.mappers.IBbsMapper;
import com.emptybeer.etb.models.PagingModel;
import com.emptybeer.etb.vos.ReviewArticleVo;
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

    public Enum<? extends IResult> reviewAdd(UserEntity user, ReviewArticleEntity reviewArticle) {
        if (this.bbsMapper.selectReviewByBeerIndex(user.getEmail(), reviewArticle.getBeerIndex()) > 0) {
            return WriteResult.NO_MORE_REVIEW;
        }
        return this.bbsMapper.insertReviewArticle(reviewArticle) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public int getReviewArticleCount(BeerEntity beer, String criterion, String keyword) {
        return this.bbsMapper.selectReviewArticleCountByBeerIndex(beer.getIndex(), criterion, keyword);
    }

    public ReviewArticleVo[] getReviewArticles(BeerEntity beer, PagingModel paging, String criterion, String keyword) {
        return this.bbsMapper.selectReviewArticleByBeerIndex(
                beer.getIndex(), criterion, keyword,
                paging.countPerPage,
                (paging.requestPage - 1) * paging.countPerPage);
    }
}
