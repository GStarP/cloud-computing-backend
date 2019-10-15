package com.nju.cloudcomputingbackend.utils;

import com.nju.cloudcomputingbackend.spark.graphx.InDegree;
import com.nju.cloudcomputingbackend.spark.graphx.OutDegree;
import com.nju.cloudcomputingbackend.spark.graphx.PageRank;
import com.nju.cloudcomputingbackend.spark.graphx.TriangleCount;

public class SparkUtil {

    public static String outDir = "/my/json";

    public static boolean updatePageRankJson() {
        try {
            PageRank.run();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean updateTriangleJson() {
        try {
            TriangleCount.run();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean updateInDegreeJson() {
        try {
            InDegree.run();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean updateOutDegreeJson() {
        try {
            OutDegree.run();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
