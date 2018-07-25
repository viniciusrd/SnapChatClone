package br.com.vinicius.android.snapchatclone.Model;

import java.io.Serializable;

public class Image implements Serializable{

    private String uid;
    private String desc;
    private String url;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
