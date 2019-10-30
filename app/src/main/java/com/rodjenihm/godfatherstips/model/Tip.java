package com.rodjenihm.godfatherstips.model;


public class Tip {
    private String rivals;
    private String time;
    private String tip;
    private String odds;
    private String status;

    public Tip(String rivals, String time, String tip, String odds, String status) {
        this.rivals = rivals;
        this.time = time;
        this.tip = tip;
        this.odds = odds;
        this.status = status;
    }

    public String getRivals() {
        return rivals;
    }

    public void setRivals(String rivals) {
        this.rivals = rivals;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Tip withRivals(String rivals) {
        this.rivals = rivals;
        return this;
    }

    public Tip withTime(String time) {
        this.time = time;
        return this;
    }

    public Tip withTip(String tip) {
        this.time = tip;
        return this;
    }

    public Tip withOdds(String odds) {
        this.odds = odds;
        return this;
    }

    public Tip withStatus(String status) {
        this.status = status;
        return this;
    }
}
