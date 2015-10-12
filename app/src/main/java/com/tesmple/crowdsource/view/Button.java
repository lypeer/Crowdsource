package com.tesmple.crowdsource.view;

/**
 * Created by lypeer on 10/10/2015.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import com.gc.materialdesign.R.id;
import com.gc.materialdesign.utils.Utils;

public abstract class Button extends com.tesmple.crowdsource.view.CustomView {
    static final String ANDROIDXML = "http://schemas.android.com/apk/res/android";
    int minWidth;
    int minHeight;
    int background;
    float rippleSpeed = 12.0F;
    int rippleSize = 3;
    Integer rippleColor;
    OnClickListener onClickListener;
    boolean clickAfterRipple = true;
    int backgroundColor = Color.parseColor("#1E88E5");
    TextView textButton;
    float x = -1.0F;
    float y = -1.0F;
    float radius = -1.0F;

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDefaultProperties();
        this.clickAfterRipple = attrs.getAttributeBooleanValue("http://schemas.android.com/apk/res-auto", "animate", true);
        this.setAttributes(attrs);
        this.beforeBackground = this.backgroundColor;
        if(this.rippleColor == null) {
            this.rippleColor = Integer.valueOf(this.makePressColor());
        }

    }

    protected void setDefaultProperties() {
        this.setMinimumHeight(Utils.dpToPx((float)this.minHeight, this.getResources()));
        this.setMinimumWidth(Utils.dpToPx((float)this.minWidth, this.getResources()));
        this.setBackgroundResource(this.background);
        this.setBackgroundColor(this.backgroundColor);
    }

    protected abstract void setAttributes(AttributeSet var1);

    public boolean onTouchEvent(MotionEvent event) {
        this.invalidate();
        if(this.isEnabled()) {
            this.isLastTouch = true;
            if(event.getAction() == 0) {
                this.radius = (float)(this.getHeight() / this.rippleSize);
                this.x = event.getX();
                this.y = event.getY();
            } else if(event.getAction() == 2) {
                this.radius = (float)(this.getHeight() / this.rippleSize);
                this.x = event.getX();
                this.y = event.getY();
                if(event.getX() > (float)this.getWidth() || event.getX() < 0.0F || event.getY() > (float)this.getHeight() || event.getY() < 0.0F) {
                    this.isLastTouch = false;
                    this.x = -1.0F;
                    this.y = -1.0F;
                }
            } else if(event.getAction() == 1) {
                if(event.getX() <= (float)this.getWidth() && event.getX() >= 0.0F && event.getY() <= (float)this.getHeight() && event.getY() >= 0.0F) {
                    ++this.radius;
                    if(!this.clickAfterRipple && this.onClickListener != null) {
                        this.onClickListener.onClick(this);
                    }
                } else {
                    this.isLastTouch = false;
                    this.x = -1.0F;
                    this.y = -1.0F;
                }
            } else if(event.getAction() == 3) {
                this.isLastTouch = false;
                this.x = -1.0F;
                this.y = -1.0F;
            }
        }

        return true;
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if(!gainFocus) {
            this.x = -1.0F;
            this.y = -1.0F;
        }

    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public Bitmap makeCircle() {
        Bitmap output = Bitmap.createBitmap(this.getWidth() - Utils.dpToPx(6.0F, this.getResources()), this.getHeight() - Utils.dpToPx(7.0F, this.getResources()), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(this.rippleColor.intValue());
        canvas.drawCircle(this.x, this.y, this.radius, paint);
        if(this.radius > (float)(this.getHeight() / this.rippleSize)) {
            this.radius += this.rippleSpeed;
        }

        if(this.radius >= (float)this.getWidth()) {
            this.x = -1.0F;
            this.y = -1.0F;
            this.radius = (float)(this.getHeight() / this.rippleSize);
            if(this.onClickListener != null && this.clickAfterRipple) {
                this.onClickListener.onClick(this);
            }
        }

        return output;
    }

    protected int makePressColor() {
        int r = this.backgroundColor >> 16 & 255;
        int g = this.backgroundColor >> 8 & 255;
        int b = this.backgroundColor >> 0 & 255;
        r = r - 30 < 0?0:r - 30;
        g = g - 30 < 0?0:g - 30;
        b = b - 30 < 0?0:b - 30;
        return Color.rgb(r, g, b);
    }

    public void setOnClickListener(OnClickListener l) {
        this.onClickListener = l;
    }

    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
        if(this.isEnabled()) {
            this.beforeBackground = this.backgroundColor;
        }

        try {
            LayerDrawable ex = (LayerDrawable)this.getBackground();
            GradientDrawable shape = (GradientDrawable)ex.findDrawableByLayerId(id.shape_bacground);
            shape.setColor(this.backgroundColor);
            this.rippleColor = Integer.valueOf(this.makePressColor());
        } catch (Exception var4) {
            ;
        }

    }

    public void setRippleSpeed(float rippleSpeed) {
        this.rippleSpeed = rippleSpeed;
    }

    public float getRippleSpeed() {
        return this.rippleSpeed;
    }

    public void setText(String text) {
        this.textButton.setText(text);
    }

    public void setTextColor(int color) {
        this.textButton.setTextColor(color);
    }

    public TextView getTextView() {
        return this.textButton;
    }

    public String getText() {
        return this.textButton.getText().toString();
    }
}
