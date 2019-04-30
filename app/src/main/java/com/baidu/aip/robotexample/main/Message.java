package com.baidu.aip.robotexample.main;

public class Message {
    public static final int ROBOT = 0;
    public static final int USER = 1;

    private int type;
    private String message;

    public Message(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
