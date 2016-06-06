package com.linda.facebattle.Util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by augustinus on 16/6/4.
 */
public class FontCustom {

    static String fongUrl = "comic.ttf";
    static Typeface tf;

    /***
     * 设置字体
     *
     * @return
     */
    public static Typeface setFont(Context context) {
        if(tf==null){
            tf = Typeface.createFromAsset(context.getAssets(), fongUrl);
        }
        return tf;
    }
}
