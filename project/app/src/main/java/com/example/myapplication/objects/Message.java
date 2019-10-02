package com.example.myapplication.objects;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    @SerializedName("body")
    private String mBody;
    @SerializedName("message_timestamp")
    private Date mMessage_timestamp;
    @SerializedName("sender")
    private String mSender;

    public Message(
            String body, String message_timestamp, String sender
    ) throws ParseException {
        this.mBody = body;
        this.mMessage_timestamp
                = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                .parse(message_timestamp);
        this.mSender = sender;
    }

    public String getmBody() {
        return mBody;
    }

    public void setmBody(String mBody) {
        this.mBody = mBody;
    }

    public Date getmMessage_timestamp() {
        return mMessage_timestamp;
    }

    public void setmMessage_timestamp(Date mMessage_timestamp) {
        this.mMessage_timestamp = mMessage_timestamp;
    }

    public String getmSender() {
        return mSender;
    }

    public void setmSender(String mSender) {
        this.mSender = mSender;
    }

}
