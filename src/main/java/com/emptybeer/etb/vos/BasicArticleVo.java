package com.emptybeer.etb.vos;

import com.emptybeer.etb.entities.bbs.BasicArticleEntity;

public class BasicArticleVo extends BasicArticleEntity {
    private String userNickname;

    public String getUserNickname() {
        return userNickname;
    }

    public BasicArticleVo setUserNickname(String userNickname) {
        this.userNickname = userNickname;
        return this;
    }
}
