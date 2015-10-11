package com.tesmple.crowdsource.utils;

import android.content.res.XmlResourceParser;

import com.tesmple.crowdsource.activity.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lypeer on 10/11/2015.
 */
public class PraseXMLUtils {

    /**
     * 获得省份和list的方法
     * @return
     */
    public static List<Map<String , String>> getProvinces(){
        List<Map<String , String>> provincesList = new ArrayList<>();
//        XmlResourceParser xrp = App.getContext().getResources().getXml(android.R.xml.provinces)
        return provincesList;
    }
}
