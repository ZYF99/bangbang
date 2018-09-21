package com.bangbang.bean;

public class Task {
    int id = 0;
    String business = "";
    String accoount = "";
    String task = "";
    int money = 0;
    String address = "";
    String state = "";
    String time = "";
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public Task(int id ,String business,String account,String task,int money,String address,String state,String time){
        this.id = id;
        this.business = business;
        this.accoount =account;
        this.task = task;
        this.money = money;
        this.address = address;
        this.state = state;
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
    public String getAccoount() {
        return accoount;
    }
    public void setAccoount(String accoount) {
        this.accoount = accoount;
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
