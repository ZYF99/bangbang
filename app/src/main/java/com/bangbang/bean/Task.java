package com.bangbang.bean;

public class Task {
    int id = 0;
    String business = "";
    String accoount_send = "";
    String accoount_received = "";
    String task = "";
    int money = 0;
    String address = "";
    String state = "";
    String time = "";
    String name_send = "";
    String name_received = "";
    public Task(int id ,String business,String account_send,String accoount_received, String task,int money,String address,String state,String time,String name_send,String name_received){
        this.id = id;
        this.business = business;
        this.accoount_send =account_send;
        this.accoount_received=accoount_received;
        this.task = task;
        this.money = money;
        this.address = address;
        this.state = state;
        this.time = time;
        this.name_send = name_send;
        this.name_received = name_received;
    }
    public String getName_send() {
        return name_send;
    }
    public void setName_send(String name_send) {
        this.name_send = name_send;
    }
    public String getName_received() {
        return name_received;
    }
    public void setName_received(String name_received) {
        this.name_received = name_received;
    }
    public String getAccoount_send() {
        return accoount_send;
    }
    public void setAccoount_send(String accoount_send) {
        this.accoount_send = accoount_send;
    }
    public String getAccoount_received() {
        return accoount_received;
    }
    public void setAccoount_received(String accoount_received) {
        this.accoount_received = accoount_received;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getBusiness() {
        return business;
    }
    public void setBusiness(String business) {
        this.business = business;
    }
    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
    }
    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }





}
