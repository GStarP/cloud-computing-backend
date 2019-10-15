package com.nju.cloudcomputingbackend.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nju.cloudcomputingbackend.model.HottestUniversityList;
import com.nju.cloudcomputingbackend.model.RankPair;
import com.nju.cloudcomputingbackend.utils.JSONUtil;
import com.nju.cloudcomputingbackend.utils.SparkUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<RankPair> getPageRankList() {
        try {
            InputStream is = new ClassPathResource("\\static\\graphx\\follows_pagerank.json").getInputStream();
            JSONObject json = JSONUtil.readJsonObject(is);
            if (null == json) {
                if (SparkUtil.updatePageRankJson()) {
                    return jsonToRankList(json, 11);
                } else {
                    return null;
                }
            } else {
                return jsonToRankList(json, 11);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RankPair> getTriangleRankList() {
        try {
            InputStream is = new ClassPathResource("\\static\\graphx\\follows_triangle_count.json").getInputStream();
            JSONObject json = JSONUtil.readJsonObject(is);
            if (null == json) {
                if (SparkUtil.updateTriangleJson()) {
                    return jsonToRankList(json, 11);
                } else {
                    return null;
                }
            } else {
                return jsonToRankList(json, 11);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RankPair getMostInDegree() {
        try {
            InputStream is = new ClassPathResource("\\static\\graphx\\at_inDegree.json").getInputStream();
            JSONObject json = JSONUtil.readJsonObject(is);
            if (null == json) {
                if (SparkUtil.updateInDegreeJson()) {
                    return jsonToRankList(json, 1).get(0);
                } else {
                    return null;
                }
            } else {
                return jsonToRankList(json, 1).get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RankPair getMostOutDegree() {
        try {
            InputStream is = new ClassPathResource("\\static\\graphx\\at_outDegree.json").getInputStream();
            JSONObject json = JSONUtil.readJsonObject(is);
            if (null == json) {
                if (SparkUtil.updateOutDegreeJson()) {
                    return jsonToRankList(json, 1).get(0);
                } else {
                    return null;
                }
            } else {
                return jsonToRankList(json, 1).get(0);
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

    private List<RankPair> jsonToRankList(JSONObject json, int length) {
        List<RankPair> res = new ArrayList<>();
        JSONArray arr = json.getJSONArray("rankList");
        for (int i = 0; i < length; i++) {
            RankPair rankPair = new RankPair();
            JSONObject obj = (JSONObject) arr.get(i);
            rankPair.setName(obj.getString("name"));
            rankPair.setRank(obj.getDouble("rank"));
            res.add(rankPair);
        }
        return res;
    }

}
