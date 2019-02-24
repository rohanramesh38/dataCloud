package com.example.rohan.datacloud;

public class profile {
    private String pic;
    private String domain;

    public profile() {
    }

    public profile(String pic, String domain) {
        this.pic = pic;
        this.domain = domain;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        pic = pic;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
