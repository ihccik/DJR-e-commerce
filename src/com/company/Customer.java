package com.company;

import java.util.List;
import java.util.UUID;

public class Customer {

    private UUID id;
    private String userName;
    private String email;
    private List<Address> address;
    private List<Long> phoneNumbers;

    public Customer(UUID id, String userName, String email) {
        this.id = id;
        this.userName = userName;
        this.email = email;
    }

    public Customer(UUID id, String userName, String email, List<Address> address) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.address = address;
    }

    public Customer(UUID id, String userName, String email, List<Address> address, List<Long> phoneNumbers) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.address = address;
        this.phoneNumbers = phoneNumbers;
    }

    public List<Long> getPhoneNumbers() {
        return phoneNumbers;
    }

    public UUID getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public List<Address> getAddress() {
        return address;
    }


}
