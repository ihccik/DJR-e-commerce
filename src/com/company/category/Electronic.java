package com.company.category;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Electronic extends Category{

    public Electronic(UUID id, String name) {
        super(id, name);
    }

    @Override
    public String findDeliveryDueDate() {

        LocalDate localDate = LocalDate.now();
        localDate = localDate.plusDays(4);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        return localDate.format(dtf);
    }

    @Override
    public String generateCategoryCode() {
        return "EL-" + getId().toString().substring(0,8);
    }
}
