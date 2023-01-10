package com.emptybeer.etb.vos;

import com.emptybeer.etb.entities.bbs.BasicCommentEntity;

public class BasicCommentVo extends BasicCommentEntity {

    private String userNickname;
    private boolean isSigned;   // 로그인을 했는가에 대한 여부
    private boolean isMine;
    private String articleTitle;

    private String boardName;

    public String getArticleTitle() {
        return articleTitle;
    }

    public BasicCommentVo setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
        return this;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public BasicCommentVo setUserNickname(String userNickname) {
        this.userNickname = userNickname;
        return this;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public BasicCommentVo setSigned(boolean signed) {
        isSigned = signed;
        return this;
    }

    public boolean isMine() {
        return isMine;
    }

    public BasicCommentVo setMine(boolean mine) {
        isMine = mine;
        return this;
    }

    public String getBoardName() {
        return boardName;
    }

    public BasicCommentVo setBoardName(String boardName) {
        this.boardName = boardName;
        return this;
    }
}
