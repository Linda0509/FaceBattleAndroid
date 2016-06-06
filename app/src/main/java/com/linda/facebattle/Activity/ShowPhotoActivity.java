package com.linda.facebattle.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.print.PrintHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.linda.facebattle.NetWork.NetJson;
import com.linda.facebattle.NetWork.NetUtil;
import com.linda.facebattle.R;
import com.linda.facebattle.Util.MD5;
import com.linda.facebattle.Util.MyToast;
import com.linda.facebattle.Util.PropertiesUtil;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * Created by augustinus on 16/6/5.
 */
public class ShowPhotoActivity extends Activity {
    private ImageView imageView;
    private Button sure , choose_another;
    private int type;
    private ProgressDialog dialog;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showphoto);
        imageView = (ImageView) findViewById(R.id.show_image);
        sure = (Button) findViewById(R.id.add_battle);
        choose_another = (Button) findViewById(R.id.choose_anther_one);

        type = getIntent().getExtras().getInt("mode");

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(ShowPhotoActivity.this);
                dialog.setMessage("please wait");
                dialog.show();
                new Thread(getTime).start();
            }
        });

        choose_another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2);
                finish();
            }
        });

         bitmap = getSmallBitmap(getIntent().getExtras().getString("uri"));

        imageView.setImageBitmap(bitmap);

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
                dialog.dismiss();
                MyToast.toast(getApplicationContext(),"network error");
            }else {

                addBattle(time);
            }

        }
    };

    public void addBattle(final String time){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Properties prop = PropertiesUtil.loadConfig(getApplicationContext());
                String uid = (String) prop.get("uid");
                String auto = (String) prop.get("authcode");
                String token = MD5.getToken(uid,auto,time);
                Message msg = new Message();
                Bundle data = new Bundle();
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("token",token);

                String linda = "";
                if (getIntent().getExtras().getString("bid").equals("-1")){
                    map.put("type", String.valueOf(type));
                    linda= NetUtil.toUploadFile(saveBitmap(bitmap),"photo",NetUtil.address+"battle/create",map);

                }else {
                    map.put("bid",getIntent().getExtras().getString("bid"));
                    linda= NetUtil.toUploadFile(saveBitmap(bitmap),"photo",NetUtil.address+"battle/join",map);

                }



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
            if (result.contains("error")){
                dialog.dismiss();
                MyToast.toast(getApplicationContext(),"network error");
            }else {
                if (result.contains("200")){
                    dialog.dismiss();
                    MyToast.toast(getApplicationContext(),"success");
                    setResult(4);
                    finish();
                }else {
                    dialog.dismiss();
                    MyToast.toast(getApplicationContext(),"error,try again.");
                }
                System.out.println(result);
            }

        }
    };

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 360,360 );
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
    public File saveBitmap(Bitmap bm) {
        File f = new File(MainActivity.ALBUM_PATH, getPhotoFileName());
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return f;
    }

    private String getPhotoFileName() {
        String username= (String) PropertiesUtil.loadConfig(getApplicationContext()).get("username");
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("_yyyyMMdd_HHmmss");
        return "IMG_"+username+"_Resume"+dateFormat.format(date) +".jpg";
    }

}
