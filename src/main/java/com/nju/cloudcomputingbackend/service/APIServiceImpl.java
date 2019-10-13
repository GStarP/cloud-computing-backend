package com.nju.cloudcomputingbackend.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nju.cloudcomputingbackend.model.HottestUniversityList;
import com.nju.cloudcomputingbackend.utils.JSONUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.InputStream;
import java.util.ArrayList;

@Service
public class APIServiceImpl implements APIService{

    @Override
    public HottestUniversityList getHottestUniversityByMonth(String time) {
        try {
            InputStream is = new ClassPathResource("\\static\\rank\\" + time + ".json").getInputStream();
            JSONObject json = JSONUtil.readJsonObject(is);
            if (null == json) {
                if (JSONUtil.updateJsonFiles(time)) {
                    return jsonToHUL(json);
                } else {
                    return null;
                }
            } else {
                return jsonToHUL(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private HottestUniversityList jsonToHUL(JSONObject json) {
        HottestUniversityList list = new HottestUniversityList();

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

        System.out.println(list.getTime());
        for (String s : list.getNameList()) {
            System.out.print(s +" ");
        }
        for (Integer i : list.getRankList()) {
            System.out.print(i +" ");
        }
        return list;
    }

}
