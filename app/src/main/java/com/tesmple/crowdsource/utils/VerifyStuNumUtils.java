package com.tesmple.crowdsource.utils;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.activity.App;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lypeer on 10/12/2015.
 */
public class VerifyStuNumUtils {

    private static RequestQueue sRequestQueue = Volley.newRequestQueue(App.getContext());

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
                StringRequest request = new StringRequest(Request.Method.POST,
                        "https://uis.uestc.edu.cn/amserver/UI/Login", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.startsWith("<!--")){
                            Message message = new Message();
                            message.what = StringUtils.VERIFY_STUNUM_SUCCESSFULLY;
                            handler.sendMessage(message);
                        }else {
                            Message message = new Message();
                            message.what = StringUtils.VERIFY_STUNUM_FAILED;
                            handler.sendMessage(message);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("erroeResponse", error.getClass().getName() + "===" + error.getMessage());
                        if(error.networkResponse != null){
                            Message message = new Message();
                            message.what = StringUtils.VERIFY_STUNUM_FAILED;
                            handler.sendMessage(message);
                        }

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("IDToken1", stuNum);
                        map.put("IDToken2", password);
//                        map.put("IDButton" , "Submit");
//                        map.put("IDToken0" , "");
//                        map.put("encoded" , "true");
//                        map.put("goto" , "aHR0cDovL3BvcnRhbC51ZXN0Yy5lZHUuY24vbG9naW4ucG9ydGFs");
//                        map.put("gx_charset" , "UTF-8");
                        return map;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
//                        map.put("Content-Type", "application/x-www-form-urlencoded");
                        map.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                        map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36");
                        return map;
                    }
                };
                sRequestQueue.add(request);
                break;

        }
    }

}
