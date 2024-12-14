package com.example.databaseproject.Model;

public class Car {
    private int carId;
    private int year;
    private String model;
    private int companyId;
    private String companyName; // Новое поле для названия компании
    private int price;

    // Конструктор
    public Car(int carId, int year, String model, int companyId, String companyName, int price) {
        this.carId = carId;
        this.year = year;
        this.model = model;
        this.companyId = companyId;
        this.companyName = companyName; // Инициализация нового поля
        this.price = price;
    }

    // Геттеры и сеттеры
    public int getCarId() {
        return carId;
    }

    public int getYear() {
        return year;
    }

    public String getModel() {
        return model;
    }

    public int getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getPrice() {
        return price;
    }
}

