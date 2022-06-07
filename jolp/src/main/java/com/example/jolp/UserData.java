package com.example.jolp;

//사용자 기본정보
public class UserData {
    private String userID, userPass, userSchool, userName;

    public UserData(){

    }
    public UserData(String userID, String userPass, String userSchool, String userName) {
        this.userID = userID;
        this.userPass = userPass;
        this.userSchool = userSchool;
        this.userName = userName;
    }
    public String getUserSchool() { return userSchool; }
    public String getUserID(){
        return userID;
    }
    public String getUserName(){
        return userName;
    }
}
