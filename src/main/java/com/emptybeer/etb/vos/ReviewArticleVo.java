package com.emptybeer.etb.vos;

import com.emptybeer.etb.entities.bbs.ReviewArticleEntity;

public class ReviewArticleVo extends ReviewArticleEntity {
    private String userNickname;

    private  boolean isSigned; // 로그인 여부
    private boolean isLiked;    // 좋아요
    private int likeCount;  // 좋아요 개수
    private String beerName; // 맥주이름
    private int totalCount;  // 후기 갯수
    private double scoreAvg;  // 후기 평점
    private boolean isDeclared; // 신고
    private int declaCount; // 신고 개수

    public boolean isDeclared() {
        return isDeclared;
    }

    public ReviewArticleVo setDeclared(boolean declared) {
        isDeclared = declared;
        return this;
    }

    public int getDeclaCount() {
        return declaCount;
    }

    public ReviewArticleVo setDeclaCount(int declaCount) {
        this.declaCount = declaCount;
        return this;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public ReviewArticleVo setSigned(boolean signed) {
        isSigned = signed;
        return this;
    }

    public ReviewArticleVo setScoreAvg(double scoreAvg) {
        this.scoreAvg = scoreAvg;
        return this;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public ReviewArticleVo setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public double getScoreAvg() {
        return scoreAvg;
    }

    public ReviewArticleVo setScoreAvg(int scoreAvg) {
        this.scoreAvg = scoreAvg;
        return this;
    }

    public String getBeerName() {
        return beerName;
    }

    public ReviewArticleVo setBeerName(String beerName) {
        this.beerName = beerName;
        return this;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public ReviewArticleVo setUserNickname(String userNickname) {
        this.userNickname = userNickname;
        return this;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public ReviewArticleVo setLiked(boolean liked) {
        isLiked = liked;
        return this;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public ReviewArticleVo setLikeCount(int likeCount) {
        this.likeCount = likeCount;
        return this;
    }
}
