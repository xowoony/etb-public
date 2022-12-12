package com.emptybeer.etb.entities.member;

import java.util.Date;
import java.util.Objects;

public class EmailAuthEntity {
    private int index;
    private String email;
    private String code;
    private String salt;
    private Date createOn;
    private Date expiresOn;
    private boolean expiredFlag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailAuthEntity that = (EmailAuthEntity) o;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public Date getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }

    public boolean isExpiredFlag() {
        return expiredFlag;
    }

    public void setExpiredFlag(boolean expiredFlag) {
        this.expiredFlag = expiredFlag;
    }
}
