package com.example.noti.model;

public class InfoOfDay {
    private String dayofweek;
    private String status;
    private String iconStatus;
    private int tempMax;
    private int tempMin;
    private int hum;
    private double wind;
    private int clouds;

    public InfoOfDay() {
    }

    public InfoOfDay(String dayofweek, String status, String iconStatus, int tempMax, int tempMin, int hum, double wind, int clouds) {
        this.dayofweek = dayofweek;
        this.status = status;
        this.iconStatus = iconStatus;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.hum = hum;
        this.wind = wind;
        this.clouds = clouds;
    }

    public String getDayofweek() {
        return dayofweek;
    }

    public void setDayofweek(String dayofweek) {
        this.dayofweek = dayofweek;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIconStatus() {
        return iconStatus;
    }

    public void setIconStatus(String iconStatus) {
        this.iconStatus = iconStatus;
    }

    public int getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public int getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
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

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }
}