package com.example.databaseproject.Model;

public class OrderDetail {
    private int orderId;
    private int orderDetailsId;
    private int carId;
    private int quantity;

    public OrderDetail(int orderId, int orderDetailsId, int carId, int quantity) {
        this.orderId = orderId;
        this.orderDetailsId = orderDetailsId;
        this.carId = carId;
        this.quantity = quantity;
    }

    // Геттеры для PropertyValueFactory
    public int getOrderId() {
        return orderId;
    }

    public int getOrderDetailsId() {
        return orderDetailsId;
    }

    public int getCarId() {
        return carId;
    }

    public int getQuantity() {
        return quantity;
    }
}
