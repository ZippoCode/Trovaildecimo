package it.prochilo.salvatore.trovaildecimo.custom_view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class CustomTextViewRobotoRegular extends AppCompatTextView {

    private Typeface mRobotoRegular;

    public CustomTextViewRobotoRegular(Context context) {
        super(context);
        inizialize();
    }

    public CustomTextViewRobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        inizialize();
    }

    public CustomTextViewRobotoRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inizialize();
    }

    private void inizialize() {
        mRobotoRegular = Typeface.createFromAsset(getContext().getAssets(), "font/roboto_regular.ttf");
    }


}
