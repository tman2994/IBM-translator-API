package com.oni.ibmtranslator.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LanguageModel implements Serializable {
    @SerializedName("language")
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @SerializedName("name")
    private String name;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @SerializedName("status")
    private String status;
}
