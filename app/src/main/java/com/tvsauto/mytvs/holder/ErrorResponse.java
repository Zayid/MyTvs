package com.tvsauto.mytvs.holder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Zayid on 3/9/2019.
 */

public class ErrorResponse {
    @SerializedName("ErrorDescription")
    @Expose
    private String errorDescription;
    @SerializedName("ErrorResponse")
    @Expose
    private Integer error;

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }
}
