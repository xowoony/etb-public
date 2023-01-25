package com.emptybeer.etb.entities.data;

import java.util.Date;
import java.util.Objects;

public class BeerLikeEntity {
    private String userEmail;
    private int beerIndex;

    private Date createdOn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeerLikeEntity that = (BeerLikeEntity) o;
        return beerIndex == that.beerIndex && Objects.equals(userEmail, that.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, beerIndex);
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

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
