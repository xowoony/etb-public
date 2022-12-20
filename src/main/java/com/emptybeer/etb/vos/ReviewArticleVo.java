package com.emptybeer.etb.vos;

import com.emptybeer.etb.entities.bbs.ReviewArticleEntity;

public class ReviewArticleVo extends ReviewArticleEntity {
    private String userNickname;
    private boolean isLiked;    // 좋아요
    private int likeCount;  // 좋아요 개수

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
