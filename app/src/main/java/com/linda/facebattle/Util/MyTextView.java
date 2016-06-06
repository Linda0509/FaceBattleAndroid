package com.linda.facebattle.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by augustinus on 16/6/4.
 */
public class MyTextView extends TextView {

    public MyTextView(Context context) {
        super(context);
        init(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyTextView(Context context, AttributeSet attrs, int defSyle) {
        super(context, attrs, defSyle);
        init(context);
    }

    /***
     * 设置字体
     *
     * @return
     */
    public void init(Context context) {
        setTypeface(FontCustom.setFont(context));

    }
}
