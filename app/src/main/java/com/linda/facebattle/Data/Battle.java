package com.linda.facebattle.Data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by augustinus on 16/6/4.
 */
public class Battle  {
    private String uid;
    private String type;
    private String bid;
    private String status;
    private Stater stater;

    public Battle(JSONObject jsonObject) throws JSONException {
        bid = jsonObject.getString("bid");
        uid = jsonObject.getString("uid");
        status = jsonObject.getString("status");
        type = jsonObject.getString("type");
        stater = new Stater(jsonObject.getJSONObject("stater"));
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Stater getStater() {
        return stater;
    }

    public void setStater(Stater stater) {
        this.stater = stater;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
