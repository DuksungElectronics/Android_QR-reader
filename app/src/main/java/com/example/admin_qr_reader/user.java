package com.example.admin_qr_reader;

import android.app.DownloadManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;


public class user {

    public String userNumber;
    public String timestamp;

    public user() {

    }

    public user(String userNumber) {
        this.userNumber = userNumber;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public String toString() { //객체의 내부상태를 문자열로 표현, 로그나 디버깅하려고..
        return "user{" +
                "userNumber ='" + userNumber + '\'' +
                ", timestamp ='" + timestamp + '\'' +
                '}';
    }
}
