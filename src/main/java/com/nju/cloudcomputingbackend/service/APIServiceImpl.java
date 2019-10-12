package com.nju.cloudcomputingbackend.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nju.cloudcomputingbackend.model.HottestUniversityList;
import com.nju.cloudcomputingbackend.utils.JSONUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class APIServiceImpl implements APIService{

    @Override
    public HottestUniversityList getHottestUniversityByMonth(String time) {
        HottestUniversityList list = new HottestUniversityList();

        String filePath = "F:\\HXW\\JavaProject\\cloud-computing-backend\\src\\main\\resources\\" + time + ".json";
        JSONObject json = JSONUtil.readJsonObject(filePath);

        list.setTime(json.getString("time"));

        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Integer> rankList = new ArrayList<>();
        JSONArray jsonList = json.getJSONArray("rankList");
        for (int i = 0; i < jsonList.size(); i++) {
            JSONObject object = (JSONObject) jsonList.get(i);
            nameList.add(object.getString("name"));
            rankList.add(object.getInteger("rank"));
        }
        list.setNameList(nameList);
        list.setRankList(rankList);

        return list;
    }

}
