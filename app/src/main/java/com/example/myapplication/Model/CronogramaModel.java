package com.example.myapplication.Model;

import java.text.Normalizer;
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

    public boolean containsDay(String day) {
        if (scheduleDays == null) return false;

        String normalizedDay = normalizeString(day);
        return scheduleDays.stream()
                .anyMatch(d -> normalizeString(d).equals(normalizedDay));
    }

    private String normalizeString(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase();
    }
}