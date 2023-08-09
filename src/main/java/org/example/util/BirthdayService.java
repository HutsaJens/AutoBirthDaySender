package org.example.util;

import java.time.LocalDate;

public class BirthdayService {

    private final String[] birthdayComponents;

    public BirthdayService(String birthday) {
        this.birthdayComponents = getDayAndMonth(birthday);
    }

    public boolean isBirthdayWithin7Days() {
        int day = Integer.parseInt(birthdayComponents[0]);
        int month = Integer.parseInt(birthdayComponents[1]);

        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(today.getYear(), month, day);
        LocalDate next7Days = today.plusDays(7);

        return birthday.isAfter(today) && birthday.isBefore(next7Days.plusDays(1));
    }

    public boolean isBirthdayThisMonth() {
        int month = Integer.parseInt(birthdayComponents[1]);
        LocalDate today = LocalDate.now();
        return today.getMonthValue() == month;
    }

    public boolean isTodayBirthday() {
        int day = Integer.parseInt(birthdayComponents[0]);
        int month = Integer.parseInt(birthdayComponents[1]);

        LocalDate today = LocalDate.now();
        return today.getMonthValue() == month && today.getDayOfMonth() == day;
    }

    private String[] getDayAndMonth(String dateString) {
        String[] birthdayComponents = new String[2];
        String[] parts = dateString.split("/");
        if (parts.length >= 2) {
            birthdayComponents[0] = parts[0];
            birthdayComponents[1] = parts[1];
        }
        return birthdayComponents;
    }
}