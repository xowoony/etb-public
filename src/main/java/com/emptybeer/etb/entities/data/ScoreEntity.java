package com.emptybeer.etb.entities.data;

import java.util.Objects;

public class ScoreEntity {
    private String userEmail;
    private int beerIndex;
    private int reviewIndex;
    private int score;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoreEntity that = (ScoreEntity) o;
        return reviewIndex == that.reviewIndex && Objects.equals(userEmail, that.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, reviewIndex);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getBeerIndex() {
        return beerIndex;
    }

    public void setBeerIndex(int beerIndex) {
        this.beerIndex = beerIndex;
    }

    public int getReviewIndex() {
        return reviewIndex;
    }

    public void setReviewIndex(int reviewIndex) {
        this.reviewIndex = reviewIndex;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
