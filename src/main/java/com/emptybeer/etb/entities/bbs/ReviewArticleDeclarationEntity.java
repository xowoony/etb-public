package com.emptybeer.etb.entities.bbs;

import java.util.Date;
import java.util.Objects;

public class ReviewArticleDeclarationEntity {
    private String userEmail;

    private int articleIndex;

    private Date createdOn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewArticleDeclarationEntity that = (ReviewArticleDeclarationEntity) o;
        return articleIndex == that.articleIndex && Objects.equals(userEmail, that.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, articleIndex);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public ReviewArticleDeclarationEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public int getArticleIndex() {
        return articleIndex;
    }

    public ReviewArticleDeclarationEntity setArticleIndex(int articleIndex) {
        this.articleIndex = articleIndex;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public ReviewArticleDeclarationEntity setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }
}
