package com.emptybeer.etb.vos;

import com.emptybeer.etb.entities.data.BeerEntity;

public class BeerVo extends BeerEntity {
    private String descriptionShort;

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public BeerVo setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
        return this;
    }
}
