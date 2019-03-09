package com.tvsauto.mytvs.holder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Zayid on 3/8/2019.
 */

public class Employee {
    @SerializedName("data")
    @Expose
    private List<List<String>> data = null;

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }
}
