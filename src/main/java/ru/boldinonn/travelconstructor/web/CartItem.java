package ru.boldinonn.travelconstructor.web;

import java.time.OffsetDateTime;

public class CartItem {

    private String name;

    private OffsetDateTime selectedTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OffsetDateTime getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(OffsetDateTime selectedTime) {
        this.selectedTime = selectedTime;
    }
}
