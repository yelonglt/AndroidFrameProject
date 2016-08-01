package com.dmall.mvp.dto;

/**
 * Created by yelong on 16/7/29.
 * mail:354734713@qq.com
 */
public class User {

    public String username;
    public String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
