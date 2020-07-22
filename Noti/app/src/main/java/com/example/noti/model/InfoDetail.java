package com.example.noti.model;

public class InfoDetail {
    private String time;
    private String date;
    private String iconDetail;
    private String status;
    private int temp;
    private int hum;
    private double wind;

    public InfoDetail() {
    }

    public InfoDetail(String time,String date, String iconDetail, String status, int temp, int hum, double wind) {
        this.time = time;
        this.date=date;
        this.iconDetail = iconDetail;
        this.status = status;
        this.temp = temp;
        this.hum = hum;
        this.wind = wind;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIconDetail() {
        return iconDetail;
    }

    public void setIconDetail(String iconDetail) {
        this.iconDetail = iconDetail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getHum() {
        return hum;
    }

    public void setHum(int hum) {
        this.hum = hum;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

}
