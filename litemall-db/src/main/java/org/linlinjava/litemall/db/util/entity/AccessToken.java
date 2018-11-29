package org.linlinjava.litemall.db.util.entity;

public class AccessToken {

    // 获取到的凭证
    private String token;
    // 凭证有效时间，单位：秒
    private int expiresIn;
    //保存时的时间(毫秒值)
    private long time;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
