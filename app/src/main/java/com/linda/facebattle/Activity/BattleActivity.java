package com.linda.facebattle.Activity;

import android.app.Activity;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.linda.facebattle.Adapter.BattleAdapter;
import com.linda.facebattle.Data.Battle;
import com.linda.facebattle.Data.SingleBattle;
import com.linda.facebattle.NetWork.NetUtil;
import com.linda.facebattle.R;
import com.linda.facebattle.Util.MD5;
import com.linda.facebattle.Util.MyToast;
import com.linda.facebattle.Util.PropertiesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by augustinus on 16/6/5.
 */
public class BattleActivity extends Activity {

    private SimpleDraweeView image1,image2;
    private ImageView lose_win;
    private TextView text1,text2;
    private SingleBattle singleBattle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle);

        image1 = (SimpleDraweeView) findViewById(R.id.your_photo);
        image2 = (SimpleDraweeView) findViewById(R.id.my_photo);
        text1 = (TextView) findViewById(R.id.your_msg);
        text2 = (TextView) findViewById(R.id.my_msg);
        lose_win = (ImageView) findViewById(R.id.lose_or_win);

        new Thread(getTime).start();

    }

    Runnable getTime = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            String time= NetUtil.getTime();
            data.putString("time", time);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String time = data.getString("time");
            assert time != null;
            if (time.contains("error")){
                MyToast.toast(BattleActivity.this,"network error");
            }else {
                get(time);
            }

        }
    };

    public void get(final String time){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle data = new Bundle();
                Map<String, String> map = new HashMap<String, String>();
                Properties prop = PropertiesUtil.loadConfig(getApplicationContext());
                map.put("uid", (String) prop.get("uid"));
                map.put("token", MD5.getToken((String) prop.get("uid"),(String) prop.get("authcode"),time));
                map.put("bid",getIntent().getExtras().getString("bid"));
                String linda= NetUtil.post(map,NetUtil.address+"battle/detail");
                data.putString("result", linda);
                msg.setData(data);
                handler2.sendMessage(msg);
            }
        }).start();




    }

    Handler handler2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String result = data.getString("result");
            assert result != null;
            System.out.println(result);
            if (result.contains("error")){
                MyToast.toast(getApplicationContext(),"network error");
            }else {
                try {
                    JSONObject a = new JSONObject(result);
                    JSONObject b = a.getJSONObject("data");
                    singleBattle = new SingleBattle(b);

                    Uri uri = Uri.parse(NetUtil.address+"photos/"+singleBattle.getStater().getPid());
                    image1.setImageURI(uri);

                    Uri uri2 = Uri.parse(NetUtil.address+"photos/"+singleBattle.getParticipator().getPid());
                    image2.setImageURI(uri2);

                    text1.setText(singleBattle.getStater().getMsg());
                    text2.setText(singleBattle.getParticipator().getMsg());

                    Properties prop = PropertiesUtil.loadConfig(getApplicationContext());
                    if (singleBattle.finalEnding((String) prop.get("uid"))){
                        lose_win.setImageResource(R.drawable.win_text);
                    }else {
                        lose_win.setImageResource(R.drawable.lost_text);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    };



}
