package com.nju.cloudcomputingbackend.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.nju.cloudcomputingbackend.model.DataNode;
import com.nju.cloudcomputingbackend.model.EdgeNode;
import com.nju.cloudcomputingbackend.model.GraphData;
import com.nju.cloudcomputingbackend.model.ItemStyle;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.*;

public class GenerateGraphJson {

    private static String schoolsPath = "\\static\\schools.json";
    private static String followUrl = "mongodb://94.191.110.118:27017";
    private static String followDB = "university_weibo";
    private static String followCol = "follows_info";
    private static String atUrl = "mongodb://heiming.xyz/27017";
    private static String atDB = "university_at_relation";
    private static String atColBase = "_at";

    public static void main(String[] args) {
        generateResultFile();
    }

    public static GraphData generate() {
        LinkedHashMap<String, String> nameMap = readSchoolMap();
        try {
            return getFollowGraphJsonStr(nameMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void generateResultFile() {
        try {
            OutputStream os = new FileOutputStream("src\\main\\resources\\static\\graphx\\result.json");
            PrintWriter writer = new PrintWriter(os);
            JSONObject obj = new JSONObject();
            GraphData graphData = generate();
            JSONArray data = JSONArray.parseArray(JSON.toJSONString(graphData.getData()));
            JSONArray edge = JSONArray.parseArray(JSON.toJSONString(graphData.getEdge()));
            obj.put("data", data);
            obj.put("edge", edge);
            writer.println(obj.toJSONString());
            writer.flush();
            os.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LinkedHashMap<String, String> readSchoolMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        try {
            InputStream is = new ClassPathResource(schoolsPath).getInputStream();
            JSONObject json = JSONUtil.readJsonObject(is);
            JSONArray array = json.getJSONArray("university_list");
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                map.put(obj.getString("id"), obj.getString("name"));
            }
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    public static GraphData getFollowGraphJsonStr(LinkedHashMap<String, String> nameMap) {
        String res = null;

        List<DataNode> dataNodes = new ArrayList<>();
        for (int i = 0; i < 108; i++) {
            dataNodes.add(new DataNode());
        }
        List<EdgeNode> edgeNodes = new ArrayList<>();

        MongoClient client = new MongoClient(new MongoClientURI(followUrl));
        MongoDatabase db = client.getDatabase(followDB);
        MongoCollection<Document> col = db.getCollection(followCol);

        client = new MongoClient(new MongoClientURI(atUrl));
        db = client.getDatabase(atDB);

        FindIterable<Document> find = col.find();
        MongoCursor<Document> it = find.iterator();
        while (it.hasNext()) {
            Document doc = it.next();
            String id = doc.getString("id");  // 当前学校id
            if (id.equals("1878134457")) {
                System.out.println("---DEBUG---");
            }
            int curIndex = getMapKeyIndex(id, nameMap);  // 当前学校的下标位置

            String atColName = id + atColBase;
            MongoCollection<Document> atCol = db.getCollection(atColName);

            List<Document> followsList = (List<Document>) doc.get("followsList");
            int atSum = 0;  // 当前学校被其他学校@总数
            for (Document curUni : followsList) {
                int atNum = 0;  // 当前学校被当前其他学校@数
                String curUniName = curUni.getString("name");  // 当前其他学校名
                try {
                    if (curUniName.substring(curUniName.length() - 2).equals("大学")) {
                        int curUniIndex = getMapValIndex(curUniName, nameMap);
                        if (curUniIndex == -1) {
                            // 当前关注者不是108学校之一
                        } else {
                            EdgeNode edgeNode = new EdgeNode();
                            edgeNode.setSource(curIndex);
                            edgeNode.setTarget(curUniIndex);
                            Bson searchKey = Filters.eq("university", curUniName);
                            FindIterable<Document> searchRes = atCol.find(searchKey);
                            MongoCursor<Document> searchResIt = searchRes.iterator();
                            if (searchResIt.hasNext()) {
                                Document d = searchResIt.next();
                                atNum = Integer.parseInt(d.getString("atNum"));
                                atSum += atNum;
                                if (atNum > 3) {
                                    edgeNode.setValue(atNum * 2);
                                    edgeNodes.add(edgeNode);
                                }
                            }
//                            edgeNode.setValue(atNum);
//                            edgeNodes.add(edgeNode);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (atSum != 0) {
                dataNodes.get(curIndex).setName(nameMap.get(id));
                dataNodes.get(curIndex).setSymbolSize(atSum / 7);
                ItemStyle itemStyle = new ItemStyle();
                itemStyle.setColor(getColorByRank(atSum));
                dataNodes.get(curIndex).setItemStyle(itemStyle);
            }
        }
        GraphData graphData = new GraphData();
        graphData.setData(dataNodes);
        graphData.setEdge(edgeNodes);
        return graphData;
    }

    /**
     * @des 获取有序字典中某值的下标
     * @param key
     * @return
     */
    public static int getMapKeyIndex(String key, LinkedHashMap<String, String> map) {
        int count = 0;
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            if (key.equals(entry.getKey())) {
                return count;
            }
            count++;
        }
        return -1;
    }

    /**
     * @des 获取有序字典中某值的下标
     * @param val
     * @return
     */
    public static int getMapValIndex(String val, LinkedHashMap<String, String> map) {
        int count = 0;
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            if (val.equals(entry.getValue())) {
                return count;
            }
            count++;
        }
        return -1;
    }

    public static String getColorByRank(int rank) {
        if (rank >= 0 && rank < 40) {
            return "#9999CC";
        } else if (rank >= 40 && rank < 80) {
            return "#FF9933";
        } else if (rank >= 80 && rank < 120) {
            return "#FF6633";
        } else if (rank >= 120 && rank < 160) {
            return "#FF3300";
        } else {
            return "#FF0000";
        }
    }

}
