package com.emptybeer.etb.vos;

import com.emptybeer.etb.entities.data.BeerEntity;

public class BeerVo extends BeerEntity {
    private String descriptionShort;

    public String categoryText;

    private boolean isSigned;   // 로그인을 했는가에 대한 여부

    private boolean isLiked;    // 좋아요

    private int likeCount;  // 좋아요 개수

    private int rank; // 인기 순위

    private int all; // 맥주 후기 전체 개수
    private int star5; // 맥주 점수 개수
    private int star4;
    private int star3;
    private int star2;
    private int star1;

    public int getAll() {
        return all;
    }

    public BeerVo setAll(int all) {
        this.all = all;
        return this;
    }

    public int getStar5() {
        return star5;
    }

    public BeerVo setStar5(int star5) {
        this.star5 = star5;
        return this;
    }

    public int getStar4() {
        return star4;
    }

    public BeerVo setStar4(int star4) {
        this.star4 = star4;
        return this;
    }

    public int getStar3() {
        return star3;
    }

    public BeerVo setStar3(int star3) {
        this.star3 = star3;
        return this;
    }

    public int getStar2() {
        return star2;
    }

    public BeerVo setStar2(int star2) {
        this.star2 = star2;
        return this;
    }

    public int getStar1() {
        return star1;
    }

    public BeerVo setStar1(int star1) {
        this.star1 = star1;
        return this;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public BeerVo setSigned(boolean signed) {
        isSigned = signed;
        return this;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public BeerVo setLiked(boolean liked) {
        isLiked = liked;
        return this;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public BeerVo setLikeCount(int likeCount) {
        this.likeCount = likeCount;
        return this;
    }

    public int getRank() {
        return rank;
    }

    public BeerVo setRank(int rank) {
        this.rank = rank;
        return this;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public BeerVo setCategoryText(String categoryText) {
        this.categoryText = categoryText;
        return this;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public BeerVo setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
        return this;
    }
}
