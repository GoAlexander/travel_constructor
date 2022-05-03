package ru.boldinonn.travelconstructor.web;

import java.time.OffsetDateTime;
import java.util.List;

public class ServiceOfProvider {
    private String name;

    private Integer price;

    private List<OffsetDateTime> availableTimes;

    public ServiceOfProvider() {
    }

    public ServiceOfProvider(String name, Integer price, List<OffsetDateTime> availableTimes) {
        this.name = name;
        this.price = price;
        this.availableTimes = availableTimes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<OffsetDateTime> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<OffsetDateTime> availableTimes) {
        this.availableTimes = availableTimes;
    }
}
