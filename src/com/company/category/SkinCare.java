package com.company.category;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class SkinCare extends Category{

    public SkinCare(UUID id, String name) {
        super(id, name);
    }

    @Override
    public String findDeliveryDueDate() {


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.now();
        localDate.format(dtf);
        return localDate.format(dtf);
    }


}
