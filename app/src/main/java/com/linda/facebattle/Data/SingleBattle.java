package com.linda.facebattle.Data;

import org.json.JSONException;
import org.json.JSONObject;

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
}
