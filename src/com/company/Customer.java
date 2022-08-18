package com.company;

import java.util.List;
import java.util.Scanner;
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

    public Customer(UUID id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        if (!email.matches("^(.+)@(.+)\\.(.+)$")){

            System.err.println("Invalid Email address try again");

            setEmail(new Scanner(System.in).nextLine());

        }else this.email = email;
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
