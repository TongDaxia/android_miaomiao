package com.tyg.miaomiao.info.dto;

import com.google.gson.Gson;

public class LoginDTO {

    String phone;
    String password;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
