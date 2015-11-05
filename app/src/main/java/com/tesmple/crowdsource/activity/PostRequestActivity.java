package com.tesmple.crowdsource.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
/*
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;*/
import com.gc.materialdesign.views.ButtonFlat;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.utils.ActivityCollector;
import com.tesmple.crowdsource.utils.BillUtils;
import com.tesmple.crowdsource.utils.LabelUtils;
import com.tesmple.crowdsource.utils.StringUtils;
import com.tesmple.crowdsource.view.ButtonRectangle;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.object.User;
import com.tesmple.crowdsource.utils.EditTextUtils;
import com.tesmple.crowdsource.utils.TimeUtils;
import com.tesmple.crowdsource.view.RevealBackgroundView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ESIR on 2015/10/10.
 */
public class PostRequestActivity extends AppCompatActivity implements RevealBackgroundView.OnStateChangeListener{

    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

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

    RevealBackgroundView vRevealBackground;

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

    /**
     * 是否是用户的长输入
     */
    private int isUserInput = 1;

    /**
     * 用户想要发布的单
     */
    private Bill newBill;

    public final android.os.Handler handler = new android.os.Handler(){
      public void handleMessage(Message msg){
          switch (msg.what){
              case StringUtils.POST_REQUEST_SUCCESSFULLY:
                  BillUtils.getBillsList(StringUtils.FRAGMENT_MY_PUBLISH).add(newBill);
                  Bundle bundle = new Bundle();
                  bundle.putString(getString(R.string.billDeadLine),newBill.getDeadline().toString());
                  Intent intent = new Intent(PostRequestActivity.this, PostBillSuccessful.class);
                  intent.putExtras(bundle);
                  startActivity(intent);
                  finish();
                  break;
              case StringUtils.POST_REQUEST_FAILED:
                  Snackbar.make(postrequestBtrecPostbill,"发送失败",Snackbar.LENGTH_LONG).show();
                  break;
          }
      }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postrequest);
        ActivityCollector.addActivity(PostRequestActivity.this);
        setupUserProfileGrid();
        setupRevealBackground(savedInstanceState);
        /*ArrayList<String> fuck  = TimeUtils.long2hourminutesecond(70000);*/
        initViewBind();
        initToolbar();
        initRadioGroup();
        initDateAndTimeButton();
        //setAwardEditText();
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
                final Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
                cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
                cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
                cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
                DatePickerDialog dialog = new DatePickerDialog(PostRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String stYear, stMonth, stDay;
                        stYear = String.valueOf(year);
                        stMonth = String.valueOf((monthOfYear < 9) ? "0" + (monthOfYear + 1) : monthOfYear + 1);
                        stDay = String.valueOf((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth);
                        postrequestBtflatDatepicker.setText(stYear + "-" + stMonth + "-" + stDay);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
                        .get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                dialog.show();
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
        postrequestEtBillDescription.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) { //当为删除键并且是按下动作时执行
                    int selectionStart = postrequestEtBillDescription.getSelectionStart();
                    int lastPos = 0;
                    for (int i = 0; i < labelUtilses.size(); i++) { //循环遍历所有标签
                        if ((lastPos = postrequestEtBillDescription.getText().toString().indexOf(labelUtilses.get(i).getLabelName(), 0)) != -1) {
                            if ((selectionStart != 0) && (selectionStart == lastPos + 1 || selectionStart == (lastPos + labelUtilses.get(i).getLabelName().length()))) {
                                Log.i("label",labelUtilses.get(i).getLabelName().length()+" "+labelUtilses.get(i).getLabelName().toString());
                                String sss = postrequestEtBillDescription.getText().toString();
                                postrequestEtBillDescription.setText(sss.substring(0, lastPos) + sss.substring(lastPos + labelUtilses.get(i).getLabelName().length())); //字符串替换，删掉符合条件的字符串
                                labelUtilses.remove(i); //删除对应实体
                                postrequestEtBillDescription.setSelection(lastPos); //设置光标位置
                                return true;
                            } else if ((selectionStart != 0) && (selectionStart > (lastPos + 1) && selectionStart < (lastPos + labelUtilses.get(i).getLabelName().length()))) {
                                String temp = labelUtilses.get(i).getLabelName();
                                temp = temp.substring(0, selectionStart - lastPos - 1) + temp.substring(selectionStart-lastPos);/*, temp.length()-selectionStart);*/
                                labelUtilses.get(i).setLabelName(temp);
                                String sss = postrequestEtBillDescription.getText().toString();
                                postrequestEtBillDescription.setText(sss.substring(0, lastPos) + temp + sss.substring(lastPos + labelUtilses.get(i).getLabelName().length() + 1)); //字符串替换，删掉符合条件的字符串
                                postrequestEtBillDescription.setSelection(selectionStart-1);
                                return true;
                            }
                        }
                    }
                    return false;
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
                int isAdd = 1;
                if (count == 1) {
                    if (s.charAt(start) == '#') {
                        if (start != 0) {
                            int selectionStart = postrequestEtBillDescription.getSelectionStart();
                            int lastPos = 0;
                            String temp = postrequestEtBillDescription.getText().toString();
                            temp = temp.substring(0, start) + temp.substring(start + 1);
                            //Log.i("temp", temp);
                            for (int i = 0; i < labelUtilses.size(); i++) { //循环遍历所有标签
                                if ((lastPos = temp.indexOf(labelUtilses.get(i).getLabelName(), 0)) != -1) {
                                    if (selectionStart > lastPos + 1 && selectionStart <= (lastPos + labelUtilses.get(i).getLabelName().length())) {
                                        isAdd = 0;
                                        break;
                                    }
                                }
                            }
                        }
                        if (isAdd == 1) {
                            String test = "#label" + nextId + "#";
                            int selectionStart = postrequestEtBillDescription.getSelectionStart();
                            int length = postrequestEtBillDescription.length();
                            if (start != postrequestEtBillDescription.getText().toString().length() + 1) {//star = length+1
                                isUserInput = 0;
                                postrequestEtBillDescription.setText(
                                        postrequestEtBillDescription.getText().toString().substring(0, start)
                                                + test
                                                + postrequestEtBillDescription.getText().toString().substring(start + 1));
                            } else {
                                isUserInput = 0;
                                postrequestEtBillDescription.setText(
                                        postrequestEtBillDescription.getText().toString().substring(0, start - 1)
                                                + test);
                            }
                            labelUtilses.add(new LabelUtils(test, nextId));
                            nextId++;
                            postrequestEtBillDescription.setSelection(start + test.length());
                        } else {
                            String temp = postrequestEtBillDescription.getText().toString();
                            temp = temp.substring(0, start) + temp.substring(start + 1);
                            isUserInput = 0;
                            postrequestEtBillDescription.setText(temp);
                            postrequestEtBillDescription.setSelection(start);
                        }
                    } else {
                        int selectionStart = postrequestEtBillDescription.getSelectionStart();
                        int lastPos = 0;
                        for (int i = 0; i < labelUtilses.size(); i++) { //循环遍历所有标签
                            String temp = postrequestEtBillDescription.getText().toString();
                            temp = temp.substring(0, start) + temp.substring(start + 1);
                            if ((lastPos = temp.indexOf(labelUtilses.get(i).getLabelName(), 0)) != -1) {
                                if (selectionStart >= lastPos && selectionStart < (lastPos + labelUtilses.get(i).getLabelName().length())) {
                                    String sss = postrequestEtBillDescription.getText().toString();
                                    labelUtilses.get(i).setLabelName(sss.substring(lastPos, lastPos + 1 + labelUtilses.get(i).getLabelName().length()).toString());
                                }
                            }
                        }
                    }
                }
                if (count > 1 && isUserInput == 1) {
                    int selectionStart = postrequestEtBillDescription.getSelectionStart();
                    int lastPos = 0;
                    String temp = postrequestEtBillDescription.getText().toString();
                    temp = temp.substring(0, start) + temp.substring(start + count);
                    for (int i = 0; i < labelUtilses.size(); i++) { //循环遍历所有标签
                        if ((lastPos = temp.indexOf(labelUtilses.get(i).getLabelName(), 0)) != -1) {
                            if (selectionStart >= lastPos && selectionStart < (lastPos + count + labelUtilses.get(i).getLabelName().length())) {
                                String sss = postrequestEtBillDescription.getText().toString();
                                labelUtilses.get(i).setLabelName(sss.substring(lastPos, lastPos + count + labelUtilses.get(i).getLabelName().length()).toString());
                            }
                        }
                    }
                }
                isUserInput = 1;
            }

            @Override
            public void afterTextChanged(Editable s) {
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
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("发布请求");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        String award = postrequestEtAward.getText().toString();
        String deadline = postrequestBtflatDatepicker.getText() + " " + postrequestBtflatTimepicker.getText()+":00";
        if (!EditTextUtils.isNumber(award) || EditTextUtils.isEmpty(award)){
            Snackbar.make(postrequestBtrecPostbill, R.string.please_input_right_award,Snackbar.LENGTH_LONG).show();
            return;
        }
        if (EditTextUtils.isEmpty(postrequestEtBillDescription.getText().toString())){
            Snackbar.make(postrequestBtrecPostbill, R.string.bill_description_cannot_be_empty,Snackbar.LENGTH_LONG).show();
            return;
        }
        java.util.Date tempDate = TimeUtils.strToDateLong(deadline);
        Long longTempTime = TimeUtils.dateToLong(tempDate);
        if (longTempTime - System.currentTimeMillis() <= 0){
            Snackbar.make(postrequestBtrecPostbill,"截止时间不可以是过去时哦(＞﹏＜)",Snackbar.LENGTH_LONG).show();
            return;
        }
        if (longTempTime - System.currentTimeMillis() <= 5 * 60 * 1000){
            Snackbar.make(postrequestBtrecPostbill,"截止时间小于5分钟哦，发布失败(＞﹏＜)",Snackbar.LENGTH_LONG).show();
            return;
        }
        if (longTempTime - System.currentTimeMillis() >= 72 * 60 * 60 *1000){
            Snackbar.make(postrequestBtrecPostbill,"截止时间超过72小时啦，发布失败(＞﹏＜)",Snackbar.LENGTH_LONG).show();
            return;
        }
        if (EditTextUtils.isEmpty(postrequestEtBillDescription.getText().toString())){
            Snackbar.make(postrequestBtrecPostbill,"具体描述不可以为空",Snackbar.LENGTH_LONG).show();
            return;
        }
        if (getContactWay().equals("000")){
            Snackbar.make(postrequestBtrecPostbill,"请选择至少一种联系途径",Snackbar.LENGTH_LONG).show();
            return;
        }

        newBill = new Bill();
        newBill.setPublisherName(User.getInstance().getNickName());
        newBill.setPublisherPhone(User.getInstance().getUserName());
        newBill.setPublisherSchool(User.getInstance().getSchool());
        newBill.setAward(award);
        newBill.setDetail(postrequestEtBillDescription.getText().toString());
        newBill.setDeadline(TimeUtils.dateToLong(tempDate));
        newBill.setAddress("");
        newBill.setStatus(StringUtils.BILL_STATUS_ONE);
        newBill.setApplicant("");
        newBill.setConfirmer("");
        newBill.setNeedNum("");
        newBill.setRobType(getRobtype());
        newBill.setLocation("");
        newBill.setAcceptDeadline("");
        newBill.setContactWay(getContactWay());
        BillUtils. publishBill(handler , newBill);
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

    public static void startUserProfileFromLocation(int[] startingLocation, Activity startingActivity) {
        Intent intent = new Intent(startingActivity, PostRequestActivity.class);
        intent.putExtra(ARG_REVEAL_START_LOCATION, startingLocation);
        startingActivity.startActivity(intent);
    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        vRevealBackground = (RevealBackgroundView)findViewById(R.id.rbv_round);

        vRevealBackground.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            final int[] startingLocation = getIntent().getIntArrayExtra(ARG_REVEAL_START_LOCATION);
            vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    vRevealBackground.startFromLocation(startingLocation);
                    return false;
                }
            });
        } else {
//            userPhotosAdapter.setLockedAnimations(true);
            vRevealBackground.setToFinishedFrame();
        }
    }

    private void setupUserProfileGrid() {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        rvUserProfile.setLayoutManager(layoutManager);
//        rvUserProfile.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                userPhotosAdapter.setLockedAnimations(true);
//            }
//        });
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            findViewById(R.id.post_sv_main).setVisibility(View.VISIBLE);
            findViewById(R.id.rbv_round).setVisibility(View.INVISIBLE);
//            userPhotosAdapter = new UserProfileAdapter(this);
//            rvUserProfile.setAdapter(userPhotosAdapter);
        } else {
            findViewById(R.id.rbv_round).setAlpha(1.0f);
            findViewById(R.id.post_sv_main).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}