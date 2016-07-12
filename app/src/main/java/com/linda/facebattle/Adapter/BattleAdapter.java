package com.linda.facebattle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linda.facebattle.Activity.BattleActivity;
import com.linda.facebattle.Activity.MainActivity;
import com.linda.facebattle.Data.Battle;
import com.linda.facebattle.R;
import com.linda.facebattle.Util.MyToast;
import com.linda.facebattle.Util.PropertiesUtil;

import java.util.List;

/**
 * Created by augustinus on 16/6/4.
 */
public class BattleAdapter extends BaseAdapter {
    private Context context;                        //运行上下文
    private List<Battle> battles;
    private LayoutInflater listContainer;           //视图容器
    private ListItemView listItemView;
    private boolean mode;

    public final class ListItemView {                //自定义控件集合
        public TextView username;
        public TextView time;
        public TextView mode;
        public ImageView avator;
    }

    public BattleAdapter(Context context,List<Battle> battles,boolean a) {
        this.context = context;
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文
        this.battles=battles;
        this.mode = a;
    }

    public BattleAdapter(Context context){
        this.context = context;
        listContainer = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return battles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = listContainer.inflate(R.layout.battle_item, null);
            listItemView = new ListItemView();
            listItemView.username= (TextView) convertView.findViewById(R.id.battle_username);
            listItemView.avator = (ImageView) convertView.findViewById(R.id.battle_ava);
            listItemView.mode = (TextView) convertView.findViewById(R.id.battle_mode);
            listItemView.time = (TextView) convertView.findViewById(R.id.battle_time);
            convertView.setTag(listItemView);
        }else {
            listItemView = (ListItemView)convertView.getTag();
        }

        listItemView.username.setText(battles.get(position).getStater().getUsername());
        listItemView.time.setText(battles.get(position).getStater().getTime().substring(5,16));
        listItemView.time.setAlpha(0.5f);
        if (battles.get(position).getType().equals("0")){
            listItemView.mode.setText("CLASSIC MODE");
        }else{
            listItemView.mode.setText("HULK MODE");
        }
        switch ((Integer.valueOf(battles.get(position).getStater().getUid())+1)%20){
            case 0:
                listItemView.avator.setImageResource(R.drawable.a1);
                break;
            case 1:
                listItemView.avator.setImageResource(R.drawable.a2);
                break;
            case 2:
                listItemView.avator.setImageResource(R.drawable.a3);
                break;
            case 3:
                listItemView.avator.setImageResource(R.drawable.a4);
                break;
            case 4:
                listItemView.avator.setImageResource(R.drawable.a5);
                break;
            case 5:
                listItemView.avator.setImageResource(R.drawable.a6);
                break;
            case 6:
                listItemView.avator.setImageResource(R.drawable.a7);
                break;
            case 7:
                listItemView.avator.setImageResource(R.drawable.a8);
                break;
            case 8:
                listItemView.avator.setImageResource(R.drawable.a9);
                break;
            case 9:
                listItemView.avator.setImageResource(R.drawable.a10);
                break;
            case 10:
                listItemView.avator.setImageResource(R.drawable.a11);
                break;
            case 11:
                listItemView.avator.setImageResource(R.drawable.a12);
                break;
            case 12:
                listItemView.avator.setImageResource(R.drawable.a13);
                break;
            case 13:
                listItemView.avator.setImageResource(R.drawable.a14);
                break;
            case 14:
                listItemView.avator.setImageResource(R.drawable.a15);
                break;
            case 15:
                listItemView.avator.setImageResource(R.drawable.a16);
                break;
            case 16:
                listItemView.avator.setImageResource(R.drawable.a17);
                break;
            case 17:
                listItemView.avator.setImageResource(R.drawable.a18);
                break;
            case 18:
                listItemView.avator.setImageResource(R.drawable.a19);
                break;
            case 19:
                listItemView.avator.setImageResource(R.drawable.a20);
                break;
            default:
                break;
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode){
                    Intent intent = new Intent(context,BattleActivity.class);
                    intent.putExtra("bid",battles.get(position).getBid());
                    context.startActivity(intent);
                }else {
                    if (battles.get(position).getStater().getUid().equals((String) PropertiesUtil.loadConfig(context).get("uid"))){
                        MyToast.toast(context,"You can not play with yourself~");

                    }else{
                        MainActivity.bid = battles.get(position).getBid();
                        MainActivity.mode = Integer.valueOf(battles.get(position).getType());
                        ((MainActivity)context).showDialog2();
                    }
                }

        }
        });
        return convertView;
    }
}
