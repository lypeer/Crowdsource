package com.tesmple.crowdsource.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gc.materialdesign.views.ButtonFlat;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.layout.ButtonRectangle;

/**
 * Created by ESIR on 2015/10/10.
 */
public class PostRequestActivity extends AppCompatActivity {

    /**
     *  postrequest界面的具体描述EditText
     */
    private EditText postrequestEtBilldescription;

    /**
     * postrequest界面的日期选择按钮
     */
    private ButtonFlat postrequestBtflatDatepicker;

    /**
     * postrequest界面的时间选择按钮
     */
    private ButtonFlat postrequestBtflatTimepicker;

    /**
     * 多选框中的电话方式
     */
    private CheckBox postrequestCbPhone;

    /**
     * 多选框中的短信方式
     */
    private CheckBox postrequestCbMessage;

    /**
     * 多选框中的站内回复方式
     */
    private CheckBox postrequestCbReplyinapp;

    /**
     * postrequest界面的模式多选框
     */
    private RadioGroup postrequestRgBillmode;

    /**
     * 模式多选框的抢单模式
     */
    private RadioButton postrequestRbGrabbillmode;

    /**
     * 模式多选框的接单模式
     */
    private RadioButton postrequestRbReceivebillmode;

    /**
     * postrequest界面的发送请求按钮
     */
    private ButtonRectangle postrequest_btrec_postbill;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postrequest);
        initViewBind();
        initToolbar();
        initRadioGroup();
    }

    /**
     * 初始化视图依赖
     */
    private void initViewBind(){
        postrequestEtBilldescription = (EditText)findViewById(R.id.postrequest_et_billdescription);
        postrequestBtflatDatepicker = (ButtonFlat)findViewById(R.id.postrequest_btflat_datepicker);
        postrequestBtflatTimepicker = (ButtonFlat)findViewById(R.id.postrequest_btflat_timepicker);

        postrequestCbPhone = (CheckBox)findViewById(R.id.postrequest_cb_phone);
        postrequestCbMessage = (CheckBox)findViewById(R.id.postrequest_cb_message);
        postrequestCbReplyinapp = (CheckBox)findViewById(R.id.postrequest_cb_replyinapp);

        postrequestRgBillmode = (RadioGroup)findViewById(R.id.postrequest_rg_billmode);
        postrequestRbGrabbillmode = (RadioButton)findViewById(R.id.postrequest_rb_grabbillmode);
        postrequestRbReceivebillmode = (RadioButton)findViewById(R.id.postrequest_rb_receivebillmode);

        postrequestCbPhone = (CheckBox)findViewById(R.id.postrequest_cb_phone);
        postrequestCbMessage = (CheckBox)findViewById(R.id.postrequest_cb_message);
        postrequestCbReplyinapp = (CheckBox)findViewById(R.id.postrequest_cb_replyinapp);

        postrequest_btrec_postbill = (ButtonRectangle)findViewById(R.id.postrequest_btrec_postbill);

        stopParentScroll(postrequestEtBilldescription);
    }

    /**
     * 当滑动事件冲突的时候拦截parent的方法
     * @param editText  目标edittext
     */
    public static void stopParentScroll(EditText editText){
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar(){
        Toolbar Toolbar = (Toolbar)findViewById(R.id.toolbar);
        Toolbar.setTitle("发布请求");
        setSupportActionBar(Toolbar);

    }

    /**
     * 初始化RadioGroup,预选
     */
    private void initRadioGroup(){
        postrequestRgBillmode.check(postrequestRbGrabbillmode.getId());
    }
}
