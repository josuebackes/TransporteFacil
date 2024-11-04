package com.example.myapplication.Model;

import java.util.List;

public class CronogramaModel {

    private String goingTime;
    private String returnTime;
    private List<String> scheduleDays;

    public CronogramaModel() {}

    public CronogramaModel(String goingTime, String returnTime, List<String> scheduleDays) {
        this.goingTime = goingTime;
        this.returnTime = returnTime;
        this.scheduleDays = scheduleDays;
    }

    public boolean containsDay(String day) {
        return scheduleDays != null &&
                scheduleDays.stream().anyMatch(d -> d.equalsIgnoreCase(day));
    }

    public String getGoingTime() {
        return goingTime;
    }

    public void setGoingTime(String goingTime) {
        this.goingTime = goingTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public List<String> getScheduleDays() {
        return scheduleDays;
    }

    public void setScheduleDays(List<String> scheduleDays) {
        this.scheduleDays = scheduleDays;
    }
}