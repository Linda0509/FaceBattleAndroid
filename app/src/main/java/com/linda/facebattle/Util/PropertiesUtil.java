package com.linda.facebattle.Util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Created by augustinus on 16/6/4.

 */
public class PropertiesUtil  {

    static public Properties loadConfig(Context context) {
        Properties properties = new Properties();
        try {
            FileInputStream s = new FileInputStream(context.getFilesDir().getPath()+"/"+"app.properties");
            properties.load(s);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }
    //保存配置文件
    static public boolean saveConfig(Context context, Properties properties) {
        try {
            File fil=new File(context.getFilesDir().getPath()+"/"+"app.properties");
            if(!fil.exists())
                fil.createNewFile();
            FileOutputStream s = new FileOutputStream(fil);
            properties.store(s, "");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
