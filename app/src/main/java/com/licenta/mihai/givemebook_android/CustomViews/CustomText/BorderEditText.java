package com.licenta.mihai.givemebook_android.CustomViews.CustomText;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.licenta.mihai.givemebook_android.R;

/**
 * Created by mihai on 23.05.2017.
 */

public class BorderEditText extends AppCompatEditText {
    public BorderEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Regular.ttf"));
        this.setSingleLine(true);
        this.setInputType(this.getInputType());
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        this.setTextColor(getResources().getColor(R.color.colorPrimaryText));
        this.setHintTextColor(getResources().getColor(R.color.colorSecondaryText)) ;

        int originalDrawable = R.drawable.custom_edit_text;
        int sdk = android.os.Build.VERSION.SDK_INT;
        int jellyBean = android.os.Build.VERSION_CODES.JELLY_BEAN;
        if (sdk < jellyBean) {
            this.setBackgroundDrawable(getResources().getDrawable(originalDrawable));
        } else {
            this.setBackground(getResources().getDrawable(originalDrawable));
        }
    }
}
