package com.linda.facebattle.NetWork;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by augustinus on 16/6/4.
 */
public class NetJson {
    private int code;
    private String msg;
    private JSONObject data;


    public NetJson(String json){
        try {
            JSONObject object = new JSONObject(json);
            code = object.getInt("code");
            msg = object.getString("msg");
            data = object.getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
