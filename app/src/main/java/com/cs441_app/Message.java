package com.cs441_app;

import java.text.SimpleDateFormat;

public class Message {
    private String content;
    private String authorName;
    private String authorID;
    private long day;
    private long month;
    private long year;

    private long hour;
    private long min;

    private String timestamp;

    public Message(String cont, String name, String id, long d, long m, long y, long h, long mi){
        content = cont;
        authorName = name;
        authorID = id;
        day = d;
        month = m;
        year = y;
        hour = h;
        min = mi;
    }

    public Message(String cont, String name, String id){
        content = cont;
        authorName = name;
        authorID = id;
        timestamp = new SimpleDateFormat("yyy.MM.dd.HH.mm.ss").format(new java.util.Date());
    }

    public Message(String cont, String name, String id, String Timestamp){
        content = cont;
        authorName = name;
        authorID = id;
        timestamp = Timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public long getMonth() {
        return month;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public long getHour() {
        return hour;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }
}
