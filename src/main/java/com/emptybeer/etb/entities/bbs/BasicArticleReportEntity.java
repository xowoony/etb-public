package com.emptybeer.etb.entities.bbs;

import java.util.Date;
import java.util.Objects;

public class BasicArticleReportEntity {
    private String userEmail;
    private int articleIndex;
    private Date createdOn;

    public String getUserEmail() {
        return userEmail;
    }

    public BasicArticleReportEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public int getArticleIndex() {
        return articleIndex;
    }

    public BasicArticleReportEntity setArticleIndex(int articleIndex) {
        this.articleIndex = articleIndex;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public BasicArticleReportEntity setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicArticleReportEntity that = (BasicArticleReportEntity) o;
        return articleIndex == that.articleIndex && Objects.equals(userEmail, that.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, articleIndex);
    }
}
