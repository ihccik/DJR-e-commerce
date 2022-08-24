package com.company.category;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Furniture extends Category{

    public Furniture(UUID id, String name) {
        super(id, name);
    }

    @Override
    public String findDeliveryDueDate() {

        LocalDate localDate = LocalDate.now();
        localDate = localDate.plusDays(1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        return localDate.format(dtf);
    }

}
