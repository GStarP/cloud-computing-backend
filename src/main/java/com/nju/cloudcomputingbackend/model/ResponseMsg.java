package com.nju.cloudcomputingbackend.model;

/**
 * @author hxw
 * @des 返回信息模型
 */
public class ResponseMsg {

    private int code;       // 状态码: 200 为正常
    private String msg;     // 信息
    private Object data;    // 数据

    /**
     * @des 创建成功响应
     * @param data 携带数据
     * @return
     */
    public static ResponseMsg buildSuccessResponse(Object data) {
        ResponseMsg res = new ResponseMsg();
        res.setCode(200);
        res.setMsg("");
        res.setData(data);
        return res;
    }

    /**
     * @des 创建失败响应
     * @param msg 携带信息
     * @return
     */
    public static ResponseMsg buildFailureResponse(String msg) {
        ResponseMsg res = new ResponseMsg();
        res.setCode(404);
        res.setMsg(msg);
        res.setData(null);
        return res;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
