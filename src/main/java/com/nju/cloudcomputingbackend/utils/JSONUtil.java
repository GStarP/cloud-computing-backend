package com.nju.cloudcomputingbackend.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;

/**
 * @author hxw
 * @des JSON 相关的工具类
 */
public class JSONUtil {

    public static JSONObject readJsonObject(String file) {
        String jsonStr = "";
        try {
            File jsonFile = new File(file);
            // FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
            jsonStr = sb.toString();
            JSONObject object = JSON.parseObject(jsonStr);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
