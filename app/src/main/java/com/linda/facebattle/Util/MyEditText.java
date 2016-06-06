package com.linda.facebattle.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by augustinus on 16/6/4.
 */
public class MyEditText extends EditText {
    public MyEditText(Context context) {
        super(context);
        init(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    public void init(Context context) {
        setTypeface(FontCustom.setFont(context));
    }
}
