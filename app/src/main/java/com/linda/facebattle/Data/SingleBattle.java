package com.linda.facebattle.Data;

import android.content.Context;

import com.linda.facebattle.Util.PropertiesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;

/**
 * Created by augustinus on 16/6/5.
 */
public class SingleBattle {
    private String bid;
    private String uid;
    private String type;
    private String status;
    private Stater stater;
    private Stater participator;

    public SingleBattle(JSONObject object) throws JSONException {
        bid = object.getString("bid");
        uid = object.getString("uid");
        type = object.getString("type");
        status = object.getString("status");
        stater = new Stater(object.getJSONObject("stater"));
        participator = new Stater(object.getJSONObject("participator"));
    }



    public Stater getParticipator() {
        return participator;
    }

    public void setParticipator(Stater participator) {
        this.participator = participator;
    }

    public Stater getStater() {
        return stater;
    }

    public void setStater(Stater stater) {
        this.stater = stater;
    }

    //就stater和participator而言
    public boolean winornot() throws JSONException {
        String a = stater.getMax();
        String b[] = a.split(":");
        String str1 = b[0];
        String c = participator.getMax();
        String d[] = c.split(":");
        String str2 = d[0];

        if (str1.equals(str2)){
            return (Integer.valueOf(b[1])>Integer.valueOf(d[1]));
        }else {
            if (str1.equals("anger") && str2.equals("sadness")){
                return false;
            }else if (str1.equals("sadness") && str2.equals("anger")){
                return true;
            }else if(str1.equals("anger") && str2.equals("happiness")){
                return true;
            }else if (str1.equals("happiness") && str2.equals("anger")){
                return false;
            }else if(str1.equals("sadness") && str2.equals("happiness")){
                return false;
            }else if (str1.equals("happiness") && str2.equals("anger")){
                return true;
            }else {
                return true;
            }
        }
    }

    public boolean finalEnding(String myuid) throws JSONException {
        boolean a = winornot();
        if (stater.getUid().equals(myuid)){
            return a;
        }else {
            return !a;
        }
    }

}
