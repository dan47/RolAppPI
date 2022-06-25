package com.example.rolapppi.utills;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimeCalculate {

    public long duration(String calving) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = java.time.LocalDate.parse(calving, formatter);
        long duration = DAYS.between(localDate, LocalDate.now());
        return duration;
    }

}
