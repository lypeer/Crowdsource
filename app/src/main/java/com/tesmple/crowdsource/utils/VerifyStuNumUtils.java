package com.tesmple.crowdsource.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.activity.App;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *  Created by lypeer on 10/12/2015.
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
                RequestParams params;
                params = new RequestParams();
                params.put("IDToken0", "");
                params.put("IDToken1", stuNum);
                params.put("IDToken2", password);
                params.put("goto", "aHR0cDovL3BvcnRhbC51ZXN0Yy5lZHUuY24vbG9naW4ucG9ydGFs");
                params.put("encoded", "true");
                params.put("gx_charset", "UTF-8");
                params.put("IDButton", "Submit");
                AsyncHttpClient client = new AsyncHttpClient();
                //允许重定向
                client.setEnableRedirects(true);
                //获取cookie
                PersistentCookieStore myCookieStore = new PersistentCookieStore(App.getContext());
                //每次发起一个请求都清空cookie,不然的话每次都是第一个请求的人的cookie
                myCookieStore.clear();
                client.setCookieStore(myCookieStore);
                client.post(url, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                                //进入信息门户，开始往教务系统里面跳转
                                Document doc = Jsoup.parse(new String(responseBody));
                                String test = doc.title();
                                if (test.equals(App.getContext().getString(R.string.uestc_info_door))) {
                                    AsyncHttpClient client = new AsyncHttpClient();
                                    PersistentCookieStore myCookieStore = new PersistentCookieStore(App.getContext());
                                    client.setCookieStore(myCookieStore);
                                    client.get("http://portal.uestc.edu.cn/index.portal?.pn=p346", new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                                            //进入教务系统，开始往iframe里面跳转
                                            AsyncHttpClient client = new AsyncHttpClient();
                                            PersistentCookieStore myCookieStore = new PersistentCookieStore(App.getContext());
                                            client.setCookieStore(myCookieStore);
                                            client.get("http://eams.uestc.edu.cn/eams/home!submenus.action", new AsyncHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                                                    //进入iframe的内嵌网址，开始往我的信息里面跳转
                                                    AsyncHttpClient client = new AsyncHttpClient();
                                                    PersistentCookieStore myCookieStore = new PersistentCookieStore(App.getContext());
                                                    client.setCookieStore(myCookieStore);
                                                    client.get("http://eams.uestc.edu.cn/eams/home!childmenus.action?menu.id=845", new AsyncHttpResponseHandler() {
                                                        @Override
                                                        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                                                            //进入我的信息页面，开始往学籍信息里面跳转
                                                            AsyncHttpClient client = new AsyncHttpClient();
                                                            PersistentCookieStore myCookieStore = new PersistentCookieStore(App.getContext());
                                                            client.setCookieStore(myCookieStore);
                                                            client.get("http://eams.uestc.edu.cn/eams/stdDetail.action", new AsyncHttpResponseHandler() {
                                                                @Override
                                                                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                                                                    Document doc2 = Jsoup.parse(new String(responseBody));
                                                                    Elements elements = doc2.getElementsByTag("td");
                                                                    Message msg = new Message();
                                                                    Bundle bundle = new Bundle();
                                                                    bundle.putString("fuckhtml", elements.text());
                                                                    msg.setData(bundle);
                                                                    msg.what = StringUtils.VERIFY_STUNUM_SUCCESSFULLY;
                                                                    handler.sendMessage(msg);
                                                                }
                                                                @Override
                                                                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                                                                    Log.e("VerifyError1" , statusCode + "===" + error.getMessage() + "===" + error.getCause());
                                                                    Message message = new Message();
                                                                    message.what  = StringUtils.NETWORK_ERROE;
                                                                    handler.sendMessage(message);
                                                                }
                                                            });
                                                        }
                                                        @Override
                                                        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                                                            Log.e("VerifyError2" , statusCode + "===" + error.getMessage() + "===" + error.getCause());
                                                            Message message = new Message();
                                                            message.what  = StringUtils.NETWORK_ERROE;
                                                            handler.sendMessage(message);
                                                        }
                                                    });
                                                }
                                                @Override
                                                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                                                    Log.e("VerifyError3" , statusCode + "===" + error.getMessage() + "===" + error.getCause());
                                                    Message message = new Message();
                                                    message.what  = StringUtils.NETWORK_ERROE;
                                                    handler.sendMessage(message);
                                                }
                                            });

                                        }
                                        @Override
                                        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                                            Log.e("VerifyError4" , statusCode + "===" + error.getMessage() + "===" + error.getCause());
                                            Message message = new Message();
                                            message.what  = StringUtils.NETWORK_ERROE;
                                            handler.sendMessage(message);
                                        }
                                    });
                                } else {
                                    Message message = new Message();
                                    message.what = StringUtils.VERIFY_STUNUM_FAILED;
                                    handler.sendMessage(message);
                                }

                            }
                            @Override
                            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                                Log.e("VerifyError5" , statusCode + "===" + error.getMessage() + "===" + error.getCause());
                                Message message = new Message();
                                message.what  = StringUtils.NETWORK_ERROE;
                                handler.sendMessage(message);
                            }
                        }
                );
                break;
        }
    }
}