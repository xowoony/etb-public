package com.emptybeer.etb.entities.bbs;

import java.util.Date;
import java.util.Objects;

public class ReviewArticleEntity {
    private int index;
    private String userEmail;
    private String boardId;
    private int beerIndex;
    private int score;
    private String contentGood;

    private String contentBad;

    private int view;
    private Date writtenOn;
    private Date modifiedOn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewArticleEntity that = (ReviewArticleEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public int getBeerIndex() {
        return beerIndex;
    }

    public void setBeerIndex(int beerIndex) {
        this.beerIndex = beerIndex;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getContentGood() {
        return contentGood;
    }

    public void setContentGood(String contentGood) {
        this.contentGood = contentGood;
    }

    public String getContentBad() {
        return contentBad;
    }

    public ReviewArticleEntity setContentBad(String contentBad) {
        this.contentBad = contentBad;
        return this;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public Date getWrittenOn() {
        return writtenOn;
    }

    public void setWrittenOn(Date writtenOn) {
        this.writtenOn = writtenOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
