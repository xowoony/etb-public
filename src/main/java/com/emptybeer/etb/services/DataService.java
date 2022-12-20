package com.emptybeer.etb.services;

import com.emptybeer.etb.entities.data.BeerEntity;
import com.emptybeer.etb.mappers.IDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "com.emptybeer.etb.services.DataService")
public class DataService {

    private final IDataMapper iDataMapper;

    @Autowired
    public DataService(IDataMapper iDataMapper) {
        this.iDataMapper = iDataMapper;
    }


}
