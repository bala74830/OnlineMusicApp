package com.example.onlinemusicapp.Model;

public class Upload {

    String name;
    String url;
    String songscategory;

    public Upload(String name, String url, String songscategory) {
        this.name = name;
        this.url = url;
        this.songscategory = songscategory;
    }

    public Upload() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getsongscategory() {
        return songscategory;
    }

    public void setsongscategory(String songscategory) {
        this.songscategory = songscategory;
    }
}
