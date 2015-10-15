package com.tesmple.crowdsource.utils;

import java.io.Serializable;

/**
 * Created by ESIR on 2015/10/13.
 */
public class LabelUtils implements Serializable {
    private static final long serialVersionUID = 1L;
    private String labelId;
    private String labelName;
    public LabelUtils(String labelName , int lableId){
        super();
        this.labelId = labelId;
        this.labelName = labelName;
    }

    public String getLabelId(){
        return labelId;
    }

    public String getLabelName(){
        return labelName;
    }

    public void setLabelId(String labelId){
        this.labelId = labelId;
    }

    public void setLabelName(String labelName){
        this.labelName = labelName;
    }
}
