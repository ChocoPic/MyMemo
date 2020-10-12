package com.lyam.mymemo;

public class MyMemo {
    private String data;
    private int color;

    public MyMemo(String data){
        this.data = data;
        this.color = R.color.myGray;
    }

    public MyMemo(String data, int color){
        this.data = data;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
