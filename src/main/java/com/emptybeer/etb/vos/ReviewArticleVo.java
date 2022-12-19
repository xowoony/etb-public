package com.emptybeer.etb.vos;

import com.emptybeer.etb.entities.bbs.ReviewArticleEntity;

public class ReviewArticleVo extends ReviewArticleEntity {
    private String userNickname;

    public String getUserNickname() {
        return userNickname;
    }

    public ReviewArticleVo setUserNickname(String userNickname) {
        this.userNickname = userNickname;
        return this;
    }
}
