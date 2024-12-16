package com.example.databaseproject.Model;

public class Customer {
    private int customerId;
    private String name;
    private String login;
    private String phoneNumber;
    private String password;

    // Конструктор
    public Customer(int customerId, String name, String login, String phoneNumber, String password) {
        this.customerId = customerId;
        this.name = name;
        this.login = login;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    // Геттеры и сеттеры
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Метод toString для вывода информации об объекте
    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
