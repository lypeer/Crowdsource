package com.tesmple.crowdsource.utils;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
/*
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;*/
import com.tesmple.crowdsource.activity.App;

import org.apache.http.Header;
import org.apache.http.params.HttpParamsNames;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by lypeer on 10/12/2015.
 */
public class VerifyStuNumUtils {


    /**
     * 验证学号的方法
     *
     * @param handler    请求验证的界面里面的handler
     * @param schoolName 学校的名字
     * @param stuNum     用户的学号
     * @param password   用户的密码
     */
    public static void verifyStuNum(final Handler handler, String schoolName, final String stuNum, final String password) {
        switch (schoolName) {
            case StringUtils.UESTC:
                String url = "https://uis.uestc.edu.cn/amserver/UI/Login";
/*
                RequestParams params = new RequestParams();
                params.put("IDToken0" , "");
                params.put("IDToken1",stuNum);
                params.put("IDToken2", password);
                params.put("goto" , "aHR0cDovL3BvcnRhbC51ZXN0Yy5lZHUuY24vbG9naW4ucG9ydGFs");
                params.put("encoded" , "true");
                params.put("gx_charset" , "UTF-8");
                params.put("IDButton" , "Submit");
                AsyncHttpClient client = new AsyncHttpClient();
                client.setEnableRedirects(true);
                client.post(url, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Document doc = Jsoup.parse(new String(responseBody));
                                String test = doc.title();
                                Log.e("respinse2" , test);
                                Log.e("respinse3" , doc.text());
                                if(test.equals("电子科技大学信息门户")){
                                    Toast.makeText(App.getContext(), "登陆成功！",
                                            Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    Toast.makeText(App.getContext(), "登陆失败,请检查密码输入",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Log.e("errpr" ,  error.getMessage() + "===" + error.getCause());
                                Log.e("test","15615614555656565");
                            }

                        }
                );*/
                break;

        }
    }
}