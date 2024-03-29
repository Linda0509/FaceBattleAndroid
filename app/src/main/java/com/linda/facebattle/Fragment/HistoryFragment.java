package com.linda.facebattle.Fragment;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.linda.facebattle.Activity.MainActivity;
import com.linda.facebattle.Adapter.BattleAdapter;
import com.linda.facebattle.Data.Battle;
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
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by augustinus on 16/6/4.
 */
public class HistoryFragment extends Fragment {
    private FloatingActionButton fab;
    private ListView listView;
    private BattleAdapter adapter;
    private List<Battle> battles;
    private SwipeRefreshLayout swipe;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_history, container, false);
        final FrameLayout view = MainActivity.addview;
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int cx = (fab.getLeft() + fab.getRight())/2 ;
                int cy = (MainActivity.close.getTop() + MainActivity.close.getBottom())/2 ;
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, 2000);
                view.setVisibility(View.VISIBLE);
                anim.start();
            }
        });



        listView = (ListView) rootView.findViewById(R.id.list2);
        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.history_swipe);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    public void getData(){
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
            battles = new ArrayList<>();
            adapter = new BattleAdapter(getActivity(),battles,false);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Bundle data = msg.getData();
            String time = data.getString("time");
            assert time != null;
            if (time.contains("error")){
                MyToast.toast(getActivity(),"network error");
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
                Properties prop = PropertiesUtil.loadConfig(getActivity());
                map.put("uid", (String) prop.get("uid"));
                map.put("token", MD5.getToken((String) prop.get("uid"),(String) prop.get("authcode"),time));
                String linda= NetUtil.post(map,NetUtil.address+"battle/finished");
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
            Log.i("msg",result);
            if (result.contains("error")){
                MyToast.toast(getActivity(),"network error");
            }else {
                try {
                    JSONObject a = new JSONObject(result);
                    JSONArray b = a.getJSONArray("data");
                    for (int i=0;i<b.length();i++){
                        if (b.get(i).toString().equals("false")){

                        }else {
                            Battle battle = new Battle(b.getJSONObject(i));
                            battles.add(battle);
                        }

                    }
                    adapter = new BattleAdapter(getActivity(),battles,true);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            swipe.setRefreshing(false);
        }
    };


}
