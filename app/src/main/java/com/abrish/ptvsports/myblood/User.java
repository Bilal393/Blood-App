package com.abrish.ptvsports.myblood;

public class User {
    private String id;
    private String name;
    private String bloodGroup;
    private String city;
    private String number;
    private String email;
    private String password;
    private String userType;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String id, String name, String bloodGroup, String city, String number, String email, String password, String userType) {
        this.id = id;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.city = city;
        this.number = number;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }
}
