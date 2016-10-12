package com.linda.facebattle.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.linda.facebattle.R;
import com.linda.facebattle.Util.PropertiesUtil;

import java.util.Properties;

/**
 * Created by augustinus on 16/6/4.
 */
public class LoadingActivity extends BaseActivity {

    private boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext());
        setContentView(R.layout.loading);
        isLogin = false;
        Properties prop = PropertiesUtil.loadConfig(getApplication());
        if (prop == null) {
            //配置文件不存在的时候创建配置文件 初始化配置信息
            prop = new Properties();
            prop.put("key", "linda");
            PropertiesUtil.saveConfig(getApplicationContext(), prop);
        } else {
            if (((String) prop.get("uid")) != null) {
                if (!((String) prop.get("uid")).equals("")) {
                    isLogin = true;
                }
            }
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin){
                    Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(LoadingActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },1000);
    }



}
