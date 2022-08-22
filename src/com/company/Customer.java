package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Stream;

public class Customer {

    private UUID id;
    private String userName;
    private String email;
    private List<Address> address;
    private List<Long> phoneNumbers = new ArrayList<>();

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

        if (StaticConstants.CUSTOMER_LIST.stream().map(Customer::getUserName).anyMatch(s -> s.equals(userName))){

            System.err.println("This user name already taken");

            setUserName(new Scanner(System.in).nextLine());

        }else this.userName = userName;
    }

    public void setEmail(String email) {

        boolean alreadyUsed = StaticConstants.CUSTOMER_LIST.stream().map(Customer::getEmail).anyMatch(s -> s.equals(email));

        if (!email.matches("^(.+)@(.+)\\.(.+)$") || alreadyUsed){

            if (alreadyUsed){
                System.err.println("There is already an account with that email");
            }else System.err.println("Invalid Email address try again");

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
