package com.nju.cloudcomputingbackend.controller;

import com.nju.cloudcomputingbackend.model.HottestUniversityList;
import com.nju.cloudcomputingbackend.model.RankPair;
import com.nju.cloudcomputingbackend.model.ResponseMsg;
import com.nju.cloudcomputingbackend.service.APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
