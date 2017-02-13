package com.example.tanay.firebaseexp;

/**
 * Created by tanay on 13/2/17.
 */
public class User {

    private String name,contact,address;

    public User(String address, String contact, String name) {
        this.address = address;
        this.contact = contact;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
