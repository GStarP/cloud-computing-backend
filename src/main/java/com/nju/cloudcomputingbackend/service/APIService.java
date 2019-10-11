package com.nju.cloudcomputingbackend.service;

import com.nju.cloudcomputingbackend.model.HottestUniversityList;

/**
 * @author hxw
 * @des API 服务层接口
 */
public interface APIService {

    /**
     * @des 根据时间获取热度最高的大学信息
     * @param time "2019-08"
     * @return { "time": "2019-05", "nameList": ["南京大学", "复旦大学"], "rankList": [544,368] }
     */
    public HottestUniversityList getHottestUniversityByMonth(String time);

}
