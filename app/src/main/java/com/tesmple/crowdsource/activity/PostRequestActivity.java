package com.tesmple.crowdsource.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.gc.materialdesign.views.ButtonFlat;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.layout.ButtonRectangle;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.object.User;
import com.tesmple.crowdsource.utils.EditTextUtils;
import com.tesmple.crowdsource.utils.TimeUtils;

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
     * 支付报酬的EditText
     */
    private AutoCompleteTextView postrequestEtAward;

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
    private ButtonRectangle postrequestBtrecPostbill;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postrequest);
        initViewBind();
        initToolbar();
        initRadioGroup();
        initDateAndTimeButton();
        setDatePicker();
        setTimePicker();
        setButtons();
    }

    /**
     * 设置date选择监听
     */
    private void setDatePicker(){
        postrequestBtflatDatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PostRequestActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String stYear, stMonth, stDay;
                        stYear = String.valueOf(year);
                        stMonth = String.valueOf((monthOfYear < 10) ? "0" + (monthOfYear + 1) : monthOfYear + 1);
                        stDay = String.valueOf((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth);
                        postrequestBtflatDatepicker.setText(stYear + "-" + stMonth + "-" + stDay);
                    }
                }, TimeUtils.getNowYear(), TimeUtils.getNowMonth() - 1, TimeUtils.getNowDay()).show();
            }
        });
    }

    private void setTimePicker(){
        postrequestBtflatTimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(PostRequestActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String stHour;
                        String stMinute;
                        if(hourOfDay < 10){
                            stHour = "0" + hourOfDay;
                        }else {
                            stHour = String.valueOf(hourOfDay);
                        }

                        if(minute < 10){
                            stMinute = "0" + minute;
                        }else {
                            stMinute = String.valueOf(minute);
                        }
                        postrequestBtflatTimepicker.setText(stHour + ":" + stMinute);
                    }
                }, TimeUtils.getNowHour(), TimeUtils.getNowMinute(), true).show();
            }
        });
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

        postrequestEtAward = (AutoCompleteTextView)findViewById(R.id.postrequest_et_award);

        postrequestRgBillmode = (RadioGroup)findViewById(R.id.postrequest_rg_billmode);
        postrequestRbGrabbillmode = (RadioButton)findViewById(R.id.postrequest_rb_grabbillmode);
        postrequestRbReceivebillmode = (RadioButton)findViewById(R.id.postrequest_rb_receivebillmode);

        postrequestCbPhone = (CheckBox)findViewById(R.id.postrequest_cb_phone);
        postrequestCbMessage = (CheckBox)findViewById(R.id.postrequest_cb_message);
        postrequestCbReplyinapp = (CheckBox)findViewById(R.id.postrequest_cb_replyinapp);

        postrequestBtrecPostbill = (ButtonRectangle)findViewById(R.id.postrequest_btrec_postbill);

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

    /**
     * 初始化日期时间按钮
     */
    private void initDateAndTimeButton(){
        postrequestBtflatDatepicker.setText(
                TimeUtils.getNowYear() + "-" + TimeUtils.getNowMonth() + "-" + TimeUtils.getNowDay()
        );
        postrequestBtflatTimepicker.setText(
                TimeUtils.getNowHour() + ":" + TimeUtils.getNowMinute()
        );
    }

    /**
     * 设置按钮监听及相关
     */
    private void setButtons(){
        postrequestBtrecPostbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempPostBill();
            }
        });
    }

    /**
     * 尝试发布订单
     */
    private void attempPostBill(){
        String publisherPhone = User.getInstance().getUserName();
        String award = postrequestEtAward.getText().toString();
        String deadline = postrequestBtflatDatepicker.getText() + " " + postrequestBtflatTimepicker.getText();
        String detail = postrequestEtBilldescription.getText().toString();
        String address;
        String status = getString(R.string.bill_status_waitingforapplicant);
        String robType = getRobtype();
        Log.i("robtypeid", String.valueOf(postrequestRgBillmode.getCheckedRadioButtonId()));
        Log.i("bill",publisherPhone + " " + award + " " + deadline + " " + detail + " " + status + " " + robType);
        Intent intent = new Intent(PostRequestActivity.this,PostBillSuccessful.class);
        startActivity(intent);
    }

    /**
     * 获得订单模式
     * 返回"抢单模式"或"接单模式"
     */
    private String getRobtype(){
        switch (postrequestRgBillmode.getCheckedRadioButtonId()){
            case R.id.postrequest_rb_grabbillmode:
                return getString(R.string.bill_robtype_grabbillmode);
            case R.id.postrequest_rb_receivebillmode:
                return getString(R.string.bill_robtype_receivebillmode);
            default:
                break;
        }
        return "Error";
    }
}
