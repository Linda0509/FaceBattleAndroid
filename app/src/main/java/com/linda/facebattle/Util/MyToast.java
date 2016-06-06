package com.linda.facebattle.Util;

import android.content.Context;
import android.widget.Toast;
import android.app.Activity;


/**
 * Created by augustinus on 16/6/4.
 */
public class MyToast  {
    public static void toast(Context context, String a){
        Toast.makeText(context,a,Toast.LENGTH_SHORT).show();
    }
}
