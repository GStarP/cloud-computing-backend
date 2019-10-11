package com.nju.cloudcomputingbackend.model;

import java.util.List;

/**
 * @author hxw
 * @des 热度最高高校列表
 */
public class HottestUniversityList {

    private String time;            // 时间
    private List<String> nameList;  // 高校名称列表
    private List<Integer> rankList; // 高校热度列表

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    public List<Integer> getRankList() {
        return rankList;
    }

    public void setRankList(List<Integer> rankList) {
        this.rankList = rankList;
    }

}
