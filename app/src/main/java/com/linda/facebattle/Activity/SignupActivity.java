package com.linda.facebattle.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.linda.facebattle.NetWork.NetJson;
import com.linda.facebattle.NetWork.NetUtil;
import com.linda.facebattle.R;
import com.linda.facebattle.Util.MyButton;
import com.linda.facebattle.Util.MyEditText;
import com.linda.facebattle.Util.MyToast;
import com.linda.facebattle.Util.PropertiesUtil;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by augustinus on 16/6/4.
 */
public class SignupActivity extends Activity{

    private TextView gotoLogin;
    private MyEditText name;
    private MyEditText pw;
    private MyEditText pw2;
    private MyButton btn;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        gotoLogin = (TextView) findViewById(R.id.gotoLogin);
        name = (MyEditText) findViewById(R.id.signup_name);
        pw = (MyEditText) findViewById(R.id.signup_pw);
        pw2 = (MyEditText) findViewById(R.id.signup_pw2);
        btn = (MyButton) findViewById(R.id.signup);

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ischeck()){
                    dialog = new ProgressDialog(SignupActivity.this);
                    dialog.setMessage("Please wait.");
                    dialog.show();
                    signup(name.getText().toString().trim(),pw.getText().toString().trim());
                }
            }
        });

    }

    public boolean ischeck(){
        if (name.getText().toString().trim().length()<4){
            MyToast.toast(this,"Username must be at least 4 letters.");
        }else if(pw.getText().toString().trim().length()<8){
            MyToast.toast(this,"Password must be at least 8 letters.");
        }else if(!pw2.getText().toString().equals(pw.getText().toString())){
            MyToast.toast(this,"Make sure two passwords are the same.");
        }else{
            return true;
        }
        return false;
    }

    public void signup(final String myname, final String mypw){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle data = new Bundle();
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", myname);
                map.put("password", mypw);
                String linda= NetUtil.post(map,NetUtil.address+"user/signup");
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
                    try {
                        String uid = json.getData().getString("uid");
                        String authcode = json.getData().getString("authcode");
                        String username = json.getData().getString("username");
                        Properties prop = PropertiesUtil.loadConfig(getApplicationContext());
                        prop.put("uid",uid);
                        prop.put("authcode",authcode);
                        prop.put("username",username);
                        PropertiesUtil.saveConfig(getApplicationContext(),prop);
                        Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    System.out.println(json.getCode());
                    MyToast.toast(SignupActivity.this,json.getMsg());
                }
            }else {
                MyToast.toast(SignupActivity.this,"register fail.");
            }
        }
    };

}
