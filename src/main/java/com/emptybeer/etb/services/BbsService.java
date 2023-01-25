package com.emptybeer.etb.services;

import com.emptybeer.etb.entities.bbs.*;
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
import com.emptybeer.etb.vos.FestivalCommentVo;
import com.emptybeer.etb.vos.ReviewArticleVo;
import org.apache.catalina.User;
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

    public BeerVo getBeerLike(int beerIndex, UserEntity signedUser) {
        return this.bbsMapper.selectBeerLikeByIndex(signedUser == null ? null : signedUser.getEmail(), beerIndex);
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


    // 리뷰 등록
    public Enum<? extends IResult> reviewAdd(UserEntity user, ReviewArticleEntity reviewArticle) {
        if (this.bbsMapper.selectReviewByBeerIndex(user.getEmail(), reviewArticle.getBeerIndex()) > 0) {
            return WriteResult.NO_MORE_REVIEW;
        }
        return this.bbsMapper.insertReviewArticle(reviewArticle) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }


    // 전체 리뷰 카운트
    public int getALLReviewArticleCount(String criterion, String keyword, String starRank) {
        return this.bbsMapper.selectAllReviewArticleCountByBeerIndex(criterion, keyword, starRank);
    }

    // 맥주별 리뷰 카운트
    public int getReviewArticleCount(BeerEntity beer, String criterion, String keyword, String starRank) {
        return this.bbsMapper.selectReviewArticleCountByBeerIndex(beer.getIndex(), criterion, keyword, starRank);
    }

    //(전체맥주) 리뷰 별점별 후기 개수
    public BeerVo getAllReviewCount() {
        return this.bbsMapper.selectAllReviewCountByBeerIndex();
    }

    // 맥주당 리뷰 항목별 보기(별점 별 후기 개수)
    public BeerVo getReviewCount(BeerVo beer) {
        return this.bbsMapper.selectReviewCountByBeerIndex(beer.getIndex());
    }

    public double getReviewAvg(BeerVo beer) {
        return this.bbsMapper.selectReviewAvgByBeerIndex(beer.getIndex()) == null ? 0 : this.bbsMapper.selectReviewAvgByBeerIndex(beer.getIndex());
    }


    //(전체 맥주) 리뷰 배열
    public ReviewArticleVo[] getAllReviewArticles(UserEntity signedUser, PagingModel paging, String criterion, String keyword, String starRank, String sort) {
        return this.bbsMapper.selectAllReviewArticleByBeerIndex(signedUser == null ? null : signedUser.getEmail(),
                criterion, keyword, starRank, sort,
                paging.countPerPage,
                (paging.requestPage - 1) * paging.countPerPage);
    }

    // 맥주별 리뷰 배열
    public ReviewArticleVo[] getReviewArticles(UserEntity signedUser, BeerEntity beer, PagingModel paging, String criterion, String keyword, String starRank, String sort) {
        return this.bbsMapper.selectReviewArticleByBeerIndex(signedUser == null ? null : signedUser.getEmail(),
                beer.getIndex(), criterion, keyword, starRank, sort,
                paging.countPerPage,
                (paging.requestPage - 1) * paging.countPerPage);
    }

    // 글 읽기 + 닉네임 불러오기
    public ReviewArticleVo reviewReadArticle(UserEntity signedUser, int index) {
        ReviewArticleVo reviewArticle = this.bbsMapper.selectLikeIndex(signedUser == null ? null : signedUser.getEmail(), index);
//        reviewArticle.setIndex(this.bbsMapper.updateReview(reviewArticle));
        return this.bbsMapper.selectLikeIndex(signedUser == null ? null : signedUser.getEmail(), index);
    }

    public ReviewArticleVo getReviewLike(UserEntity signedUser, int index) {
        return this.bbsMapper.selectLikeIndex(signedUser == null ? null : signedUser.getEmail(), index);
    }


    // 리뷰 수정
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
        reviewArticle.setBeerName(existingArticle.getBeerName());
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


    // 리뷰 삭제
    public Enum<? extends IResult> deleteReview(ReviewArticleVo reviewArticle, UserEntity user) {
        ReviewArticleVo existingArticle = this.bbsMapper.selectIndex(reviewArticle.getIndex());
        if (existingArticle == null) {
            return ArticleModifyResult.NO_SUCH_ARTICLE;
        }
        if (user == null || !(user.getEmail().equals(existingArticle.getUserEmail())|| user.getEmail().equals("admin@admin"))) {
            return ArticleModifyResult.NOT_ALLOWED;
        }

        return this.bbsMapper.deleteReviewByIndex(reviewArticle.getIndex()) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    // 리뷰 좋아요(추천)
    public Enum<? extends IResult> reviewLike(ReviewArticleLikeEntity reviewArticleLike, UserEntity user) {
        if (user == null) {
            return CommonResult.FAILURE;
        }
        reviewArticleLike.setUserEmail(user.getEmail());
        return this.bbsMapper.insertReviewLike(reviewArticleLike) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    // 리뷰 좋아요 취소
    public Enum<? extends IResult> reviewUnlike(ReviewArticleLikeEntity reviewArticleLike, UserEntity user) {
        if (user == null) {
            return CommonResult.FAILURE;
        }
        reviewArticleLike.setUserEmail(user.getEmail());
        return this.bbsMapper.deleteReviewLike(reviewArticleLike) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    // 리뷰 신고
    public Enum<? extends IResult> reviewDecla(ReviewArticleDeclarationEntity reviewArticleDeclaration, UserEntity user) {
        if (user == null) {
            return CommonResult.FAILURE;
        }
        reviewArticleDeclaration.setUserEmail(user.getEmail());
        return this.bbsMapper.insertReviewDecla(reviewArticleDeclaration) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }


    // 신고 게시물 리스트 카운트
    public int getReportedReviewCount(String criterion, String keyword) {
        return this.bbsMapper.selectReportedReviewCount(criterion, keyword);
    }

    //(신고 맥주) 리뷰 배열
    public ReviewArticleVo[] getReportedReviews(UserEntity signedUser, PagingModel paging, String criterion, String keyword, String sort) {
        return this.bbsMapper.selectReportedReviews(signedUser == null ? null : signedUser.getEmail(),
                criterion, keyword, sort,
                paging.countPerPage,
                (paging.requestPage - 1) * paging.countPerPage);
    }

    // 신고 수 초기화
    public Enum<? extends IResult> resetReport(ReviewArticleDeclarationEntity reviewArticleDeclaration, UserEntity user) {

        if (user == null || !(user.getEmail().equals("admin@admin"))) {
            return CommonResult.FAILURE;
        }

        return this.bbsMapper.deleteReportByArticleIndex(reviewArticleDeclaration.getArticleIndex()) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }
}
