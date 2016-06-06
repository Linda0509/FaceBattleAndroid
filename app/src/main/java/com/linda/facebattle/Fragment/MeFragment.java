package com.linda.facebattle.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linda.facebattle.Activity.LoginActivity;
import com.linda.facebattle.Activity.MainActivity;
import com.linda.facebattle.R;
import com.linda.facebattle.Util.PropertiesUtil;

import org.w3c.dom.Text;

import java.util.Properties;

/**
 * Created by augustinus on 16/6/4.
 */
public class MeFragment extends Fragment {

    private ImageView photo;
    private Button btn1;
    private Button btn2;
    private TextView name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_me, container, false);

        photo = (ImageView) rootView.findViewById(R.id.me_photo);
        final Properties prop = PropertiesUtil.loadConfig(getActivity());
        photo.setImageResource(R.drawable.a9);
        name = (TextView) rootView.findViewById(R.id.me_name);
        name.setText((String)prop.get("username"));

        btn2 = (Button) rootView.findViewById(R.id.logout);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties properties = PropertiesUtil.loadConfig(getActivity());
                properties.put("uid","");
                properties.put("username","");
                properties.put("token","");
                PropertiesUtil.saveConfig(getActivity(),properties);
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return rootView;

    }
}
