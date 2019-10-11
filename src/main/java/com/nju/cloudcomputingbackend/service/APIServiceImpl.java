package com.nju.cloudcomputingbackend.service;

import com.nju.cloudcomputingbackend.model.HottestUniversityList;
import org.springframework.stereotype.Service;

@Service
public class APIServiceImpl implements APIService{

    @Override
    public HottestUniversityList getHottestUniversityByMonth(String time) {
        HottestUniversityList list = new HottestUniversityList();
        // TODO
        return list;
    }

}
