package com.tesmple.crowdsource.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.gc.materialdesign.views.ButtonFlat;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.utils.LabelUtils;
import com.tesmple.crowdsource.view.ButtonRectangle;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.object.User;
import com.tesmple.crowdsource.utils.EditTextUtils;
import com.tesmple.crowdsource.utils.TimeUtils;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * postrequest界面的加标签按钮
     */
    private ButtonFlat postrequestBtflatAddLabel;

    /**
     * 存放标签的list
     */
    private ArrayList<LabelUtils> labelUtilses = new ArrayList<LabelUtils>();

    /**
     * 标签是否成对，成对为1，不成对为0
     */
    private int isLabelPair = 0;

    /**
     * 标签存储位置
     */
    private int nextId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postrequest);
        initViewBind();
        initToolbar();
        initRadioGroup();
        initDateAndTimeButton();
        setAwardEditText();
        setDescriptionEditText();
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

    /**
     * 设置time选择监听
     */
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
     * 限制支付奖励的数值为两位小数
     */
    private void setAwardEditText(){
        postrequestEtAward.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    s.delete(posDot + 3, posDot + 4);
                }
            }
        });
    }

    /**
     * 设置描述界面的添加标签能力
     */
    private void setDescriptionEditText(){
        postrequestBtflatAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "#测试测试" + nextId + "# ";
                SpannableString ss = new SpannableString(str);
                ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                postrequestEtBillDescription.setText(postrequestEtBillDescription.getText());
                postrequestEtBillDescription.append(str);
                postrequestEtBillDescription.setSelection(postrequestBtflatAddLabel.getText().toString().length());
                labelUtilses.add(new LabelUtils(str, nextId));
                nextId++;
            }
        });
        postrequestEtBillDescription.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) { //当为删除键并且是按下动作时执行
                    int selectionStart = postrequestEtBillDescription.getSelectionStart();
                    int lastPos = 0;
                    for (int i = 0; i < labelUtilses.size(); i++) { //循环遍历所有标签
                        if ((lastPos = postrequestEtBillDescription.getText().toString().indexOf(labelUtilses.get(i).getLabelName(), 0)) != -1) {
                            if (selectionStart != 0 && selectionStart > lastPos && selectionStart <= (lastPos + labelUtilses.get(i).getLabelName().length())) {
                                String sss = postrequestEtBillDescription.getText().toString();
                                postrequestEtBillDescription.setText(sss.substring(0, lastPos) + sss.substring(lastPos + labelUtilses.get(i).getLabelName().length())); //字符串替换，删掉符合条件的字符串
                                labelUtilses.remove(i); //删除对应实体
                                postrequestEtBillDescription.setSelection(lastPos); //设置光标位置
                                return true;
                            }
                        } /*else {
                            if ((lastPos + ("#" + labelUtilses.get(i).getLabelName() + "#").length()) > postrequestEtBillDescription.length()){
                                lastPos = 0;
                            }else {
                                lastPos += ("#" + labelUtilses.get(i).getLabelName() + "#").length();
                            }
                            }*/
                        }
                }
                return false;
            }
        });
        postrequestEtBillDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1 && s.charAt(start) == '#') {
                    String test = "#test"+nextId+"#";
                    int length = postrequestEtBillDescription.length();
                    postrequestEtBillDescription.setText(
                            postrequestEtBillDescription.getText().toString().substring(0, start)
                                    + test
                                    + postrequestEtBillDescription.getText().toString().substring(start + 1, length));
                    labelUtilses.add(new LabelUtils(test, nextId));
                    nextId++;
                    postrequestEtBillDescription.setSelection(start+test.length());
                    Log.i("selectionposition", String.valueOf(postrequestEtBillDescription.getSelectionStart()));
                    Log.i("selectionposition", String.valueOf(postrequestEtBillDescription.getSelectionEnd()));
                    Log.i("input", String.valueOf(s.charAt(start)));
                }
                if(count == 1){
                    int selectionStart = postrequestEtBillDescription.getSelectionStart();
                    int lastPos = 0;
                    for (int i = 0; i < labelUtilses.size(); i++) { //循环遍历所有标签
                        if ((lastPos = postrequestEtBillDescription.getText().toString().indexOf(labelUtilses.get(i).getLabelName(), 0)) != -1) {
                            if (selectionStart >= lastPos && selectionStart < (lastPos + labelUtilses.get(i).getLabelName().length())) {
                                String sss = postrequestEtBillDescription.getText().toString();
                                labelUtilses.get(i).setLabelName(sss.substring(lastPos,lastPos + 1 + labelUtilses.get(i).getLabelName().length()).toString());
                                //postrequestEtBillDescription.setText(sss.substring(0, lastPos) + sss.substring(lastPos + labelUtilses.get(i).getLabelName().length())); //字符串替换，删掉符合条件的字符串*/
                                //labelUtilses.remove(i); //删除对应实体
                                //postrequestEtBillDescription.setSelection(lastPos); //设置光标位置
                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*int TEXT_CHANGE_LISTENER_FLAG = 0;
                int findPos = 0;
                int copyPos = 0;
                String sText = s.toString();
                List<Integer> spanIndexes = new ArrayList<Integer>();
                s.clear();
                for (int i = 0; i < labelUtilses.size(); i++) {
                    String tempBookName = "#" + labelUtilses.get(i).getLabelName() + "#";
                    if ((findPos = sText.indexOf(tempBookName, findPos)) != -1) {
                        spanIndexes.add(findPos);//labelname 的开始索引，键值为偶数，从0开始
                        spanIndexes.add(findPos + tempBookName.length()); //labelname 的结束索引，键值为奇数，从1开始
                    }
                }
                if (spanIndexes != null && spanIndexes.size() != 0) {
                    for (int i = 0; i < spanIndexes.size(); i++) {
                        if (i % 2 == 0) {
                            s.append(sText.substring(copyPos, spanIndexes.get(i)));
                        } else {
                            Spanned htmlText = Html.fromHtml("<font color='blue'>" + sText.substring(copyPos, spanIndexes.get(i)) + "</font>");
                            s.append(htmlText);
                        }
                        copyPos = spanIndexes.get(i);
                    }
                    s.append(sText.substring(copyPos));
                } else {
                    s.append(sText);
                }*/
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

        postrequestBtflatAddLabel = (ButtonFlat)findViewById(R.id.postrequest_btflat_addlabel);

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
        /*AVObject bill = new AVObject("Bill");
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
        bill.put("location", "null");
        bill.put("accept_deadline", "null");
        bill.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(PostRequestActivity.this, PostBillSuccessful.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.i("error", e.getMessage());
                }
            }
        });*/
        //Toast.makeText(PostRequestActivity.this,"post",Toast.LENGTH_LONG);
        Intent intent = new Intent(PostRequestActivity.this, PostBillSuccessful.class);
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

    /**
     * 获得联系方式选择框的结果
     * 返回一个3位二进制数字字符串，从高到低依次为电话、短信、站内信方式的布尔值
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