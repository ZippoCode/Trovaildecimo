package it.prochilo.salvatore.trovaildecimo.custom_view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class CustomTextViewRobotoMedium extends AppCompatTextView {

    private Typeface mRobotoMedium;

    public CustomTextViewRobotoMedium(Context context) {
        super(context);
        inizialize();
    }

    public CustomTextViewRobotoMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        inizialize();
    }

    public CustomTextViewRobotoMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inizialize();
    }

    private void inizialize() {
        mRobotoMedium = Typeface.createFromAsset(getContext().getAssets(), "font/roboto_medium.ttf");
    }

}
