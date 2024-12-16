package com.example.databaseproject.Model;

import java.util.Date;

public class Order {
    private int orderId;
    private int customerId;
    private double amount;
    private Date orderDate;
    private int carId;

    // Конструктор
    public Order(int orderId, int customerId, double amount, Date orderDate, int carId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.orderDate = orderDate;
        this.carId = carId;
    }

    // Геттеры и сеттеры
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }
}
