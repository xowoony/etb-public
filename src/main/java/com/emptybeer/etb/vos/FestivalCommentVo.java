package com.emptybeer.etb.vos;

import com.emptybeer.etb.entities.bbs.FestivalCommentEntity;

public class FestivalCommentVo extends FestivalCommentEntity {

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
