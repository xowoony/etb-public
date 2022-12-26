package com.emptybeer.etb.vos;

import com.emptybeer.etb.entities.bbs.ReviewArticleEntity;

public class ReviewArticleVo extends ReviewArticleEntity {
    private String userNickname;
    private boolean isLiked;    // 좋아요
    private int likeCount;  // 좋아요 개수
    private String beerName;
    private int totalCount;
    private double scoreAvg;

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
