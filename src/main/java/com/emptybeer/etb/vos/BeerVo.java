package com.emptybeer.etb.vos;

import com.emptybeer.etb.entities.data.BeerEntity;

public class BeerVo extends BeerEntity {
    private String descriptionShort;

    public String categoryText;

    private boolean isSigned;   // 로그인을 했는가에 대한 여부

    private boolean isLiked;    // 좋아요

    private int likeCount;  // 좋아요 개수

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
