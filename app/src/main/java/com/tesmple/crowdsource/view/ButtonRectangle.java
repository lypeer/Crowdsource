package com.tesmple.crowdsource.view;

/**
 * Created by lypeer on 10/10/2015.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.gc.materialdesign.R.drawable;
import com.gc.materialdesign.utils.Utils;

public class ButtonRectangle extends com.tesmple.crowdsource.view.Button {
    TextView textButton;
    int paddingTop;
    int paddingBottom;
    int paddingLeft;
    int paddingRight;
    Integer height;
    Integer width;

    public ButtonRectangle(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDefaultProperties();
    }

    protected void setDefaultProperties() {
        super.minWidth = 80;
        super.minHeight = 36;
        super.background = drawable.background_button_rectangle;
        super.setDefaultProperties();
    }

    protected void setAttributes(AttributeSet attrs) {
        int bacgroundColor = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "background", -1);
        if(bacgroundColor != -1) {
            this.setBackgroundColor(this.getResources().getColor(bacgroundColor));
        } else {
            this.background = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "background", -1);
            if(this.background != -1) {
                this.setBackgroundColor(this.background);
            }
        }

        String value = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "padding");
        String text = null;
        int textResource = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "text", -1);
        if(textResource != -1) {
            text = this.getResources().getString(textResource);
        } else {
            text = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "text");
        }

        if(text != null) {
            this.textButton = new TextView(this.getContext());
            this.textButton.setText(text);
            this.textButton.setTextColor(-1);
            this.textButton.setTypeface((Typeface)null, 1);
            LayoutParams params = new LayoutParams(-2, -2);
            params.addRule(13, -1);
            params.setMargins(Utils.dpToPx(5.0F, this.getResources()), Utils.dpToPx(5.0F, this.getResources()), Utils.dpToPx(5.0F, this.getResources()), Utils.dpToPx(5.0F, this.getResources()));
            this.textButton.setLayoutParams(params);
            this.addView(this.textButton);
            int textColor = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "textColor", -1);
            if(textColor != -1) {
                this.textButton.setTextColor(textColor);
            } else {
                textColor = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "textColor", -1);
                if(textColor != -1) {
                    this.textButton.setTextColor(textColor);
                }
            }

            int[] array = new int[]{16842901};
            TypedArray values = this.getContext().obtainStyledAttributes(attrs, array);
            float textSize = values.getDimension(0, -1.0F);
            values.recycle();
            if(textSize != -1.0F) {
                this.textButton.setTextSize(textSize);
            }
        }

        this.rippleSpeed = attrs.getAttributeFloatValue("http://schemas.android.com/apk/res-auto", "rippleSpeed", (float)Utils.dpToPx(6.0F, this.getResources()));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(this.x != -1.0F) {
            Rect src = new Rect(0, 0, this.getWidth() - Utils.dpToPx(6.0F, this.getResources()), this.getHeight() - Utils.dpToPx(7.0F, this.getResources()));
            Rect dst = new Rect(Utils.dpToPx(6.0F, this.getResources()), Utils.dpToPx(6.0F, this.getResources()), this.getWidth() - Utils.dpToPx(6.0F, this.getResources()), this.getHeight() - Utils.dpToPx(7.0F, this.getResources()));
            canvas.drawBitmap(this.makeCircle(), src, dst, (Paint)null);
            this.invalidate();
        }

    }

    public void setText(String text) {
        this.textButton.setText(text);
    }
}