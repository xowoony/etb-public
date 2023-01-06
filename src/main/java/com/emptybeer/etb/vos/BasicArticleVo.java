package com.emptybeer.etb.vos;

import com.emptybeer.etb.entities.bbs.BasicArticleEntity;

public class BasicArticleVo extends BasicArticleEntity {
    private String userNickname;
    private int commentCount;

    public String getUserNickname() {
        return userNickname;
    }

    public BasicArticleVo setUserNickname(String userNickname) {
        this.userNickname = userNickname;
        return this;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public BasicArticleVo setCommentCount(int commentCount) {
        this.commentCount = commentCount;
        return this;
    }
}
