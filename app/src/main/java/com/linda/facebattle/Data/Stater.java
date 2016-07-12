package com.linda.facebattle.Data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by augustinus on 16/6/5.
 */
public class Stater {
    private String id;
    private String bid;
    private String uid;
    private String pid;
    private String time;
    private String username;

    private JSONObject score;

    public Stater(JSONObject a) throws JSONException {
        id = a.getString("id");
        bid = a.getString("bid");
        uid = a.getString("uid");
        pid = a.getString("pid");
        time = a.getString("time");
        username = a.getString("username");
        if(a.toString().contains("score")){

            if (a.get("score").toString().equals("null")){
                score = new JSONObject("{\n" +
                        "                \"anger\":0.000000001,\n" +
                        "                \"contempt\":0.000000001,\n" +
                        "                \"disgust\":0.000000001,\n" +
                        "                \"fear\":0.000000001,\n" +
                        "                \"happiness\":0.000000001,\n" +
                        "                \"neutral\":0.000000001,\n" +
                        "                \"sadness\":0.000000001,\n" +
                        "                \"surprise\":0.000000001\n" +
                        "            }");
            }else{
                if (a.getJSONObject("score")!=null){
                    score = a.getJSONObject("score");
                }
            }
        }

    }

    public String getMax() throws JSONException {
        int anger = (int)(score.getDouble("anger")*100);
        int happiness = (int)(score.getDouble("happiness")*100);
        int sadness = (int)(score.getDouble("sadness")*100);
        if (anger > happiness){
            if (anger>sadness){
                return ("anger:"+String.valueOf(anger));
            }else {
                return ("sadness:"+String.valueOf(sadness));
            }
        }else {
            if (happiness>sadness){
                return ("happiness:"+String.valueOf(happiness));
            }else {
                return ("sadness:"+String.valueOf(sadness));
            }
        }
    }

    public String getMsg() throws JSONException {
        String origin = getMax();
        String[] split = origin.split(":");
        return split[0]+" is "+split[1] + "%";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
