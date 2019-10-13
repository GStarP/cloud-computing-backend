package com.nju.cloudcomputingbackend.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.nju.cloudcomputingbackend.model.MonthRank;
import org.bson.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author hxw
 * @des JSON 相关的工具类
 */
public class JSONUtil {

    private static String mongoHost = "http://heiming.xyz";
    private static int mongoPort = 27017;
    private static String dbName = "university_heat";
    private static String universityListFile = "\\static\\university_list.txt";
    private static int likeNumW = 1;
    private static int commentNumW = 2;
    private static int repostNumW = 3;
    private static double lowDownW = 0.5;

    public static JSONObject readJsonObject(InputStream is) {
        String jsonStr;
        try {
            Reader reader = new InputStreamReader(is,"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            is.close();
            reader.close();
            jsonStr = sb.toString();
            JSONObject object = JSON.parseObject(jsonStr);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新某月的 .json 缓存
     * @param time
     * @return boolean 更新是否成功
     */
    public static boolean updateJsonFiles(String time) {
        boolean res = true;
        System.out.println("----开始更新 json 文件----");

        MongoClient client = new MongoClient(mongoHost, mongoPort);
        System.out.println("----连接 " + mongoHost + ":" + ("" + mongoPort) + " 成功----");

        MongoDatabase db = client.getDatabase(dbName);
        System.out.println("----连接数据库 " + dbName + " 成功----");

        ArrayList<String[]> uniList = getUniversityList();
        MonthRank monthRank = new MonthRank();
        // 初始化
        monthRank.setTime(time);
        monthRank.setList(new LinkedHashMap<>());

        for (int i = 0; i < uniList.size(); i++) {
            String colName = "university_heat" + ((i+1) + "");
            MongoCollection<Document> col = db.getCollection(colName);
            System.out.println("----获取集合 " + colName + " 成功----");

            FindIterable<Document> find = col.find();
            MongoCursor<Document> it = find.iterator();
            Document cur;
            while (it.hasNext()) {
                Document doc = it.next();
                String uniName = uniList.get(i)[1];
                BigInteger total = BigInteger.valueOf(
                        doc.getInteger("likeNum") * likeNumW +
                        doc.getInteger("repostNum") * repostNumW +
                        doc.getInteger("commentNum") * commentNumW
                );
                String month = doc.getString("month");
                if (month.equals(time)) {
                    monthRank.getMap().put(uniName, total);
                    break;
                }
            }
        }

        // 写入 .json 文件
        JSONObject obj = new JSONObject();
        obj.put("time", time);
        obj.put("rankList", monthRank.getTopFive());
        try {
            String filePath = ResourceUtils.getURL("classpath:").getPath() + "src\\main\\resources\\test\\" + time + ".json";
            FileWriter writer = new FileWriter(filePath);
            PrintWriter printWriter = new PrintWriter(writer);
            printWriter.write(obj.toJSONString());
            printWriter.println();
            writer.close();
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            res = false;
        }
        return res;
    }

    /**
     * @des 从.txt文件中读取高校列表
     * @return [高校id，高校名称]
     */
    public static ArrayList<String[]> getUniversityList() {
        try {
            ArrayList<String[]> res = new ArrayList<>();
            InputStream is = new ClassPathResource(universityListFile).getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            while (null != line) {
                String[] tmp = line.split(" ");
                res.add(tmp);
            }
            br.close();
            isr.close();
            is.close();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @des 将int型的年月格式化成字符串
     * @param year 2019
     * @param month 8
     * @return "2019-08"
     */
    public static String getTimeStr(int year, int month) {
        String res = "" + year;
        if (month < 10) {
            res += "0";
        }
        res += ("" + month);
        return res;
    }
}
