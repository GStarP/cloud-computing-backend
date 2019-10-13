package com.nju.cloudcomputingbackend.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigInteger;
import java.util.*;

/**
 * @author hxw
 * @des 月份标识的热度排名
 */
public class MonthRank {

    private String time;
    private LinkedHashMap<String, BigInteger> map;

    public JSONArray getTopFive() {
        List<Map.Entry<String, BigInteger>> list = new ArrayList<Map.Entry<String, BigInteger>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, BigInteger>>() {
            @Override
            public int compare(Map.Entry<String, BigInteger> o1, Map.Entry<String, BigInteger> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        JSONArray array = new JSONArray();
        for (int i = 0; i < 5; i++) {
            JSONObject object = new JSONObject();
            object.put("name", list.get(i).getKey());
            object.put("rank", list.get(i).getValue());
            array.add(object);
        }
        return array;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public LinkedHashMap<String, BigInteger> getMap() {
        return map;
    }

    public void setList(LinkedHashMap<String, BigInteger> map) {
        this.map = map;
    }
}
