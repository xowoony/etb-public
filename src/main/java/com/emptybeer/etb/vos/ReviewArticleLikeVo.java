package com.emptybeer.etb.vos;

import com.emptybeer.etb.entities.bbs.ReviewArticleLikeEntity;

public class ReviewArticleLikeVo extends ReviewArticleLikeEntity {
    private String title;
    private String contentGood;
    private String contentBad;
    private String userNickname;
    private String beerName;
    private int score;


    public String getTitle() {
        return title;
    }

    public ReviewArticleLikeVo setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContentGood() {
        return contentGood;
    }

    public ReviewArticleLikeVo setContentGood(String contentGood) {
        this.contentGood = contentGood;
        return this;
    }

    public String getContentBad() {
        return contentBad;
    }

    public ReviewArticleLikeVo setContentBad(String contentBad) {
        this.contentBad = contentBad;
        return this;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public ReviewArticleLikeVo setUserNickname(String userNickname) {
        this.userNickname = userNickname;
        return this;
    }

    public String getBeerName() {
        return beerName;
    }

    public ReviewArticleLikeVo setBeerName(String beerName) {
        this.beerName = beerName;
        return this;
    }

    public int getScore() {
        return score;
    }

    public ReviewArticleLikeVo setScore(int score) {
        this.score = score;
        return this;
    }
}