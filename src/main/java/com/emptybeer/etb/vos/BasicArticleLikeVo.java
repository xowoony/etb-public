package com.emptybeer.etb.vos;

import com.emptybeer.etb.entities.bbs.BasicArticleLikeEntity;

public class BasicArticleLikeVo extends BasicArticleLikeEntity {
    private String title;
    private int commentCount;

    private String boardName;

    public String getTitle() {
        return title;
    }

    public BasicArticleLikeVo setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public BasicArticleLikeVo setCommentCount(int commentCount) {
        this.commentCount = commentCount;
        return this;
    }

    public String getBoardName() {
        return boardName;
    }

    public BasicArticleLikeVo setBoardName(String boardName) {
        this.boardName = boardName;
        return this;
    }
}
