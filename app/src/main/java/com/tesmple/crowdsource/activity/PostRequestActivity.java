package com.tesmple.crowdsource.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.gc.materialdesign.views.ButtonFlat;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.utils.LabelUtils;
import com.tesmple.crowdsource.utils.ActivityCollector;
import com.tesmple.crowdsource.view.ButtonRectangle;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.object.User;
import com.tesmple.crowdsource.utils.EditTextUtils;
import com.tesmple.crowdsource.utils.TimeUtils;

import java.sql.Time;

/**
 * Created by ESIR on 2015/10/10.
 */
public class PostRequestActivity extends AppCompatActivity {

    /**
     *  postrequest界面的具体描述EditText
     */
    private EditText postrequestEtBillDescription;

    /**
     * postrequest界面的具体描述Linearlayout
     */
    private LinearLayout postrequestLiBillDescription;

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
                        stMonth = String.valueOf((monthOfYear < 9) ? "0" + (monthOfYear + 1) : monthOfYear + 1);
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
                        stHour = String.valueOf((hourOfDay < 10) ? "0" + (hourOfDay) : hourOfDay);
                        stMinute = String.valueOf((minute < 10) ? "0" + (minute) : minute);
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
        postrequestEtBillDescription = (EditText)findViewById(R.id.postrequest_et_billdescription);
        postrequestLiBillDescription = (LinearLayout)findViewById(R.id.postrequest_li_billdescription);
        postrequestLiBillDescription.requestFocus();
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
        postrequestCbMessage = (CheckBox) findViewById(R.id.postrequest_cb_message);
        postrequestCbReplyinapp = (CheckBox) findViewById(R.id.postrequest_cb_replyinapp);

        postrequestBtrecPostbill = (ButtonRectangle) findViewById(R.id.postrequest_btrec_postbill);

        stopParentScroll(postrequestEtBillDescription);
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
        String stYear, stMonth, stDay;
        stYear = String.valueOf(TimeUtils.getNowYear());
        stMonth = String.valueOf((TimeUtils.getNowMonth() < 10) ? "0" + (TimeUtils.getNowMonth() ) : TimeUtils.getNowMonth() );
        stDay = String.valueOf((TimeUtils.getNowDay() < 10) ? "0" + TimeUtils.getNowDay() : TimeUtils.getNowDay());
        postrequestBtflatDatepicker.setText(stYear + "-" + stMonth + "-" + stDay);

        String stHour;
        String stMinute;
        stHour = String.valueOf((TimeUtils.getNowHour() < 10) ? "0" + (TimeUtils.getNowHour()) : TimeUtils.getNowHour());
        stMinute = String.valueOf((TimeUtils.getNowMinute() < 10) ? "0" + (TimeUtils.getNowMinute()) : TimeUtils.getNowMinute());
        postrequestBtflatTimepicker.setText(stHour + ":" + stMinute);
    }

    /**
     * 设置按钮监听及相关
     */
    private void setButtons() {
        postrequestLiBillDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postrequestEtBillDescription.requestFocus();
                InputMethodManager imm = (InputMethodManager) PostRequestActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(postrequestEtBillDescription, InputMethodManager.SHOW_FORCED);
            }
        });
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
        String detail = postrequestEtBillDescription.getText().toString();
        String contactWay = getContactWay();
        String status = getString(R.string.bill_status_waitingforapplicant);
        String robType = getRobtype();
        if(EditTextUtils.isNumber(award)){
            Snackbar.make(postrequestBtrecPostbill,"请输入正确的支付报酬(仅限数字)",Snackbar.LENGTH_LONG).show();
            return;
        }
        Log.i("robtypeid", String.valueOf(postrequestRgBillmode.getCheckedRadioButtonId()));
        Log.i("bill", publisherPhone + " " + award + " " + deadline + " " + detail + " " + status + " " + robType);
        AVObject bill = new AVObject("Bill");
        bill.put("publisher_phone",User.getInstance().getUserName());
        bill.put("award",award);
        bill.put("detail",detail);
        bill.put("contact_way",contactWay);
        bill.put("status",status);
        bill.put("rob_type",robType);
        bill.put("deadline",deadline);
        bill.put("applicant","");
        bill.put("confirmer","");
        bill.put("address","null");
        bill.put("need_num","null");
        bill.put("location","null");
        bill.put("accept_deadline", "null");
        /*bill.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Intent intent = new Intent(PostRequestActivity.this, PostBillSuccessful.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.i("error",e.getMessage());
                }
            }
        });*/
        Intent intent = new Intent(PostRequestActivity.this, PostBillSuccessful.class);
        startActivity(intent);
        finish();
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

    /**
     *
     */
    private String getContactWay(){
        int checkPhone = 0,checkMessage = 0,checkReplyInApp = 0;
        if(postrequestCbPhone.isChecked()){
            checkPhone = 1;
        }
        if(postrequestCbMessage.isChecked()){
            checkMessage = 1;
        }
        if(postrequestCbReplyinapp.isChecked()){
            checkReplyInApp = 1;
        }
        return String.valueOf(checkPhone) + String.valueOf(checkMessage) + String.valueOf(checkReplyInApp);
    }
}