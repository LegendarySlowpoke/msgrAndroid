package com.restur.msgrtest.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ModelUser implements Serializable {

    private Long id;
    private String userTag;
    private String userName;
    private String userSurname;
    private String userPhone;

    public static ModelUser toModelUserFromJson(JSONObject JSONModel) {
        ModelUser modelUser = new ModelUser();
        try {
            modelUser.setId(JSONModel.getLong("id"));
            modelUser.setUserTag(JSONModel.getString("userTag"));
            modelUser.setUserName(JSONModel.getString("userName"));
            modelUser.setUserSurname(JSONModel.getString("userSurname"));
            modelUser.setUserPhone(JSONModel.getString("userPhone"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return modelUser;
    }

    //Setters & getters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserTag() {
        return userTag;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
