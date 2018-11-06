package com.bangbang.bean;

public class Task {
    int id = 0;
    String business = "";
    String account_send = "";
    String account_received = "";
    String task = "";
    int money = 0;
    String address = "";
    String state = "";
    String time = "";
    String name_send = "";
    String name_received = "";
    public Task(int id ,String business,String account_send,String account_received, String task,int money,String address,String state,String time,String name_send,String name_received){
        this.id = id;
        this.business = business;
        this.account_send =account_send;
        this.account_received=account_received;
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
    public String getAccount_send() {
        return account_send;
    }
    public void setAccount_send(String account_send) {
        this.account_send = account_send;
    }
    public String getAccount_received() {
        return account_received;
    }
    public void setAccount_received(String account_received) {
        this.account_received = account_received;
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
