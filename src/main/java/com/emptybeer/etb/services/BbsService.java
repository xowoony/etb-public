package com.emptybeer.etb.services;

import com.emptybeer.etb.entities.bbs.BoardEntity;
import com.emptybeer.etb.entities.bbs.ReviewArticleEntity;
import com.emptybeer.etb.entities.data.BeerEntity;
import com.emptybeer.etb.entities.data.BeerLikeEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.enums.bbs.ArticleModifyResult;
import com.emptybeer.etb.enums.bbs.WriteResult;
import com.emptybeer.etb.interfaces.IResult;
import com.emptybeer.etb.mappers.IBbsMapper;
import com.emptybeer.etb.models.PagingModel;
import com.emptybeer.etb.vos.BeerVo;
import com.emptybeer.etb.vos.ReviewArticleVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

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

    public BeerVo getBeer(int beerIndex) {
        return this.bbsMapper.selectBeerByIndex(beerIndex);
    }

    // 맥주 좋아요
    public Enum<? extends IResult> beerLike(BeerLikeEntity beerLike, UserEntity user) {
        if (user == null) {
            return CommonResult.FAILURE;
        }
        beerLike.setUserEmail(user.getEmail());
        return this.bbsMapper.insertBeerLike(beerLike) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    // 맥주 좋아요 취소
    public Enum<? extends IResult> beerUnlike(BeerLikeEntity beerLike, UserEntity user) {
        if (user == null) {
            return CommonResult.FAILURE;
        }
        beerLike.setUserEmail(user.getEmail());
        return this.bbsMapper.deleteBeerLike(beerLike) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
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

    // 글 읽기 + 닉네임 불러오기
    public ReviewArticleVo reviewReadArticle(UserEntity signedUser, int index) {
        ReviewArticleVo reviewArticle = this.bbsMapper.selectLikeIndex(signedUser == null ? null : signedUser.getEmail(), index);
        reviewArticle.setIndex(this.bbsMapper.updateReview(reviewArticle));
        return this.bbsMapper.selectLikeIndex(signedUser == null ? null : signedUser.getEmail(), index);
    }


    // 게시글 수정
    public Enum<? extends IResult> prepareModifyReview(ReviewArticleVo reviewArticle, UserEntity user) {

        if (user == null) {
            return ArticleModifyResult.NOT_SIGNED;
        }
        ReviewArticleVo existingArticle = this.bbsMapper.selectIndex(reviewArticle.getIndex());

        if (existingArticle == null) {
            return ArticleModifyResult.NO_SUCH_ARTICLE;
        }

        if (!user.getEmail().equals(existingArticle.getUserEmail())) {
            return ArticleModifyResult.NOT_ALLOWED;
        }

        // POST일 경우만 담아주면 된다?
        reviewArticle.setIndex(existingArticle.getIndex());
        reviewArticle.setUserEmail(existingArticle.getUserEmail());
        reviewArticle.setBoardId(existingArticle.getBoardId());
        reviewArticle.setBeerIndex(existingArticle.getBeerIndex());
        reviewArticle.setScore(existingArticle.getScore());
        reviewArticle.setContentGood(existingArticle.getContentGood());
        reviewArticle.setContentBad(existingArticle.getContentBad());
        reviewArticle.setDeclaration(existingArticle.getDeclaration());
        reviewArticle.setWrittenOn(existingArticle.getWrittenOn());
        reviewArticle.setModifiedOn(existingArticle.getModifiedOn());
        return CommonResult.SUCCESS;
    }


    public Enum<? extends IResult> modifyReview(ReviewArticleVo reviewArticle, UserEntity user) {
        if (user == null) {
            return ArticleModifyResult.NOT_SIGNED;
        }
        ReviewArticleVo existingArticle = this.bbsMapper.selectIndex(reviewArticle.getIndex());
        if (existingArticle == null) {
            return ArticleModifyResult.NO_SUCH_ARTICLE;
        }
        if (!existingArticle.getUserEmail().equals(user.getEmail())) {
            return ArticleModifyResult.NOT_ALLOWED;
        }

        existingArticle.setScore(reviewArticle.getScore());
        existingArticle.setContentGood(reviewArticle.getContentGood());
        existingArticle.setContentBad(reviewArticle.getContentBad());
        existingArticle.setModifiedOn(new Date());
        return this.bbsMapper.updateReview(existingArticle) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }
}
