package com.nju.cloudcomputingbackend.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nju.cloudcomputingbackend.model.*;
import com.nju.cloudcomputingbackend.service.APIService;
import com.nju.cloudcomputingbackend.utils.SparkUtil;
import com.nju.cloudcomputingbackend.utils.GenerateGraphJson;
import com.nju.cloudcomputingbackend.utils.JSONUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.List;

/**
 * @author hxw
 * @des 提供 API 接口
 */
@RestController
@CrossOrigin("*")
public class APIController {

    @Autowired
    private APIService apiService;

    @GetMapping("/hot")
    public ResponseMsg getHottestUniversityByMonth(@RequestParam String time) {
        HottestUniversityList list;
        try {
            list = apiService.getHottestUniversityByMonth(time);
            if (null == list) {
                return ResponseMsg.buildFailureResponse("资源不存在!");
            }
            return ResponseMsg.buildSuccessResponse(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMsg.buildFailureResponse("获取失败!");
        }
    }

    @GetMapping("/page-rank")
    public ResponseMsg getPageRankList() {
        List<RankPair> rankList;
        try {
            rankList = apiService.getPageRankList();
            if (null == rankList) {
                return ResponseMsg.buildFailureResponse("资源不存在!");
            }
            return ResponseMsg.buildSuccessResponse(rankList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMsg.buildFailureResponse("获取失败!");
        }
    }

    @GetMapping("/triangle-rank")
    public ResponseMsg getTriangleRankList() {
        List<RankPair> rankList;
        try {
            rankList = apiService.getTriangleRankList();
            if (null == rankList) {
                return ResponseMsg.buildFailureResponse("资源不存在!");
            }
            return ResponseMsg.buildSuccessResponse(rankList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMsg.buildFailureResponse("获取失败!");
        }
    }

    @GetMapping("/most-in-degree")
    public ResponseMsg getMostInDegree() {
        RankPair rankPair;
        try {
            rankPair = apiService.getMostInDegree();
            if (null == rankPair) {
                return ResponseMsg.buildFailureResponse("资源不存在!");
            }
            return ResponseMsg.buildSuccessResponse(rankPair);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMsg.buildFailureResponse("获取失败!");
        }
    }

    @GetMapping("/most-out-degree")
    public ResponseMsg getMostOutDegree() {
        RankPair rankPair;
        try {
            rankPair = apiService.getMostOutDegree();
            if (null == rankPair) {
                return ResponseMsg.buildFailureResponse("资源不存在!");
            }
            return ResponseMsg.buildSuccessResponse(rankPair);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMsg.buildFailureResponse("获取失败!");
        }
    }

    @GetMapping("/graph")
    public ResponseMsg getGraph() {
        GraphData graph;
        try {
            InputStream is = new ClassPathResource("\\static\\graphx\\result.json").getInputStream();
            JSONObject obj = JSONUtil.readJsonObject(is);
            if (null == obj) {
                graph = GenerateGraphJson.generate();
            } else {
                JSONArray dataJson = obj.getJSONArray("data");
                JSONArray edgeJson = obj.getJSONArray("edge");
                List<DataNode> data = JSONObject.parseArray(dataJson.toJSONString(), DataNode.class);
                List<EdgeNode> edge = JSONObject.parseArray(edgeJson.toJSONString(), EdgeNode.class);
                graph = new GraphData();
                graph.setData(data);
                graph.setEdge(edge);
            }
            return ResponseMsg.buildSuccessResponse(graph);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMsg.buildFailureResponse("获取失败!");
        }
    }
}
