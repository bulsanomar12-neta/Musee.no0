package com.example.musee.classes;

import android.os.Parcel;

import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String userName;
    private String phoneNum;
    private String address;
    private String photo;
    private ArrayList<String> UserPieces;

    public User(String firstName, String lastName, String userName, String phoneNum, String address) {}

    public User(String firstName, String lastName, String userName, String phoneNum, String address, String photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.phoneNum = phoneNum;
        this.address = address;
        this.photo = photo;
        this.UserPieces = new ArrayList<>();
    }

    public User(Parcel in) {
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + userName + '\'' +
                ", phone='" + phoneNum + '\'' +
                ", address='" + address + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserMame(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ArrayList<String> getUserPieces() {
        return UserPieces;
    }

    public void setUserPieces(ArrayList<String> userPieces) {
        this.UserPieces = userPieces;
    }

}
