package com.example.user.dawak;

import java.io.Serializable;

public class Pill implements Serializable {
    private String pillName;
    private String timeOfTaken;
    private long currentTime;
    private int numberOfTaken;
    private String takenDay;

    public Pill(){
    }


    public Pill(String pillName, String timeOfTaken, long currentTime, int numberOfTaken, String takenDay) {
        this.pillName = pillName;
        this.timeOfTaken = timeOfTaken;
        this.currentTime = currentTime;
        this.numberOfTaken = numberOfTaken;
        this.takenDay = takenDay;
    }


    public String getTakenDay() {
        return takenDay;
    }

    public void setTakenDay(String takenDay) {
        this.takenDay = takenDay;
    }
    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public void setTimeOfTaken(String timeOfTaken) {
        this.timeOfTaken = timeOfTaken;
    }

    public String getPillName() {
        return pillName;
    }

    public String getTimeOfTaken() {
        return timeOfTaken;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public int getNumberOfTaken() {
        return numberOfTaken;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;

    }

    public void setNumberOfTaken(int numberOfTaken) {
        this.numberOfTaken = numberOfTaken;
    }

}
