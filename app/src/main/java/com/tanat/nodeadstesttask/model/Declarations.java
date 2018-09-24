
package com.tanat.nodeadstesttask.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Declarations {

    @SerializedName("page")
    @Expose
    private Page page;
    @SerializedName("items")
    @Expose
    private List<Declaration> items = null;
    @SerializedName("error")
    @Expose
    private Integer error;
    @SerializedName("message")
    @Expose
    private String message;

    public Page getPage() {
        return page;
    }

    public List<Declaration> getItems() {
        return items;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
