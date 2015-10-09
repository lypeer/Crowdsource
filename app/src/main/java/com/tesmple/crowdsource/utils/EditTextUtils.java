package com.tesmple.crowdsource.utils;

import android.support.design.widget.Snackbar;

import com.tesmple.crowdsource.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ESIR on 2015/10/8.
 */
public class EditTextUtils {
    /**
     * 判断输入的号码是否手机号的方法
     *
     * @param userNumber 输入的号码
     * @return 如果是手机号，就返回true，如果不是手机号，就返回false
     */
    public static boolean isPhoneNumber(String userNumber) {
        if (userNumber.length() == 11) {
            Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|" +
                    "(17[0,7,9])|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(userNumber);
            /*if (!m.matches()) {
                Snackbar.make(btnRegister, R.string.input_right_phone, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }*/
            return m.matches();
        } else {
            /*Snackbar.make(btnRegister, R.string.input_right_phone, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();*/
            return false;
        }
    }

    /**
     * 判断输入的密码是否正确
     *
     * @param userPassword 输入的密码
     * @return 如果正确就返回true，不正确就返回false
     */
    public static boolean isPassword(String userPassword) {
        if (userPassword.length() >= 6 && userPassword.length() <= 16) {
            Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
            Matcher matcher = pattern.matcher(userPassword);
            return matcher.matches();
        } else {
            return false;
        }
    }
}