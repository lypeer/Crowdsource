package com.tesmple.crowdsource.utils;

import android.content.res.XmlResourceParser;
import android.util.Log;

import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.activity.App;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lypeer on 10/11/2015.
 */
public class ParseXMLUtils {

    /**
     * 获得省份的list的方法
     *
     * @return 返回包含省份和它的索引的map的list
     */
    public static List<Map<String, String>> getProvincesList() {
        List<Map<String, String>> provincesList = new ArrayList<>();
        XmlResourceParser xrp = App.getContext().getResources().getXml(R.xml.provinces);
        try {
            while (xrp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlPullParser.START_TAG) {
                    String tagName = xrp.getName();
                    Log.e("zxc", xrp.getAttributeValue(null, "ID") + "===" + xrp.getAttributeValue(null, "ProvinceName"));
                    if (tagName.equals("Province")) {
                        Map<String, String> map = new HashMap<>();
                        map.put("id", xrp.getAttributeValue(0));
                        map.put("name", xrp.getAttributeValue(1));
                        provincesList.add(map);
                    }
                }
                xrp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return provincesList;
    }

    /**
     * 获得城市的list的方法
     *
     * @param pId 省份的id
     * @return 包含城市的名字和id的map的list
     */
    public static ArrayList<String> getCitiesList(String pId) {
        ArrayList<String> citesList = new ArrayList<>();
        XmlResourceParser xrp = App.getContext().getResources().getXml(R.xml.cities);
        try {
            while (xrp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xrp.getEventType() != XmlPullParser.START_TAG) {

                } else if (!xrp.getName().equals("City")) {

                } else if (xrp.getAttributeValue(2).equals(pId)) {
                    citesList.add(xrp.getAttributeValue(1));
                }
                xrp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return citesList;
    }
}
