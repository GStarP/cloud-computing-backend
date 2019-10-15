package com.nju.cloudcomputingbackend.model;

import java.util.List;

public class GraphData {

    private List<DataNode> data;
    private List<EdgeNode> edge;

    public List<DataNode> getData() {
        return data;
    }

    public void setData(List<DataNode> data) {
        this.data = data;
    }

    public List<EdgeNode> getEdge() {
        return edge;
    }

    public void setEdge(List<EdgeNode> edge) {
        this.edge = edge;
    }
}
