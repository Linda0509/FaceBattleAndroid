package com.linda.facebattle.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.linda.facebattle.NetWork.NetJson;
import com.linda.facebattle.NetWork.NetUtil;
import com.linda.facebattle.R;
import com.linda.facebattle.Util.MyButton;
import com.linda.facebattle.Util.MyEditText;
import com.linda.facebattle.Util.MyTextView;
import com.linda.facebattle.Util.MyToast;
import com.linda.facebattle.Util.PropertiesUtil;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by augustinus on 16/6/4.
 */
public class LoginActivity extends Activity {

    private MyTextView gotoRegister;
    private MyEditText name;
    private MyEditText pw;
    private MyButton btn;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        gotoRegister = (MyTextView) findViewById(R.id.gotoRegister);

        name = (MyEditText) findViewById(R.id.login_name);
        pw = (MyEditText) findViewById(R.id.login_pw);
        btn = (MyButton) findViewById(R.id.login);

        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ischeck()){
                    dialog = new ProgressDialog(LoginActivity.this);
                    dialog.setMessage("Please wait.");
                    dialog.show();
                    login(name.getText().toString().trim(),pw.getText().toString().trim());
                }
            }
        });




    }

    public boolean ischeck(){
        if (name.getText().toString().trim().length()<4){
            MyToast.toast(this,"Username must be at least 4 letters.");
        }else if(pw.getText().toString().trim().length()<8){
            MyToast.toast(this,"Password must be at least 8 letters.");
        }else {
            return true;
        }
        return false;
    }

    public void login(final String myname, final String mypw){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle data = new Bundle();
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", myname);
                map.put("password", mypw);
                String linda= NetUtil.post(map,NetUtil.address+"user/login");
                data.putString("result", linda);
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }).start();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String result = data.getString("result");
            assert result != null;
            dialog.dismiss();
            if(result.contains("code")){
                NetJson json = new NetJson(result);
                if (json.getCode() == 200){
                    MyToast.toast(LoginActivity.this,json.getMsg());
                    try {
                        String uid = json.getData().getString("uid");
                        String authcode = json.getData().getString("authcode");
                        String username = json.getData().getString("username");
                        Properties prop = PropertiesUtil.loadConfig(getApplicationContext());
                        prop.put("uid",uid);
                        prop.put("authcode",authcode);
                        prop.put("username",username);
                        PropertiesUtil.saveConfig(getApplicationContext(),prop);
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    System.out.println(json.getCode());
                    MyToast.toast(LoginActivity.this,json.getMsg());
                }
            }else {
                MyToast.toast(LoginActivity.this,"login fail.");
            }
        }
    };

}
