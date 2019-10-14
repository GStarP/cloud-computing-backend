package com.nju.cloudcomputingbackend.service;

import com.nju.cloudcomputingbackend.model.HottestUniversityList;
import com.nju.cloudcomputingbackend.model.RankPair;

import java.util.List;

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

    /**
     * @des 获取 PageRank 结果
     * @return [{ "name": "南京大学", "rank": 2.0145}]
     */
    public List<RankPair> getPageRankList();

    public List<RankPair> getTriangleRankList();

    public RankPair getMostInDegree();

    public RankPair getMostOutDegree();

}
