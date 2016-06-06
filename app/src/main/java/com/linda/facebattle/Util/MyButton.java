package com.linda.facebattle.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by augustinus on 16/6/4.
 */
public class MyButton extends Button {
    public MyButton(Context context) {
        super(context);
        init(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        setTypeface(FontCustom.setFont(context));
    }
}
