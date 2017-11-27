package edu.uco.tlawyer.whereyouat;

import java.util.ArrayList;

/**
 * Created by user on 11/2/2017.
 */

public class UserClass {

    String email;
    String username;
    String name;
    String phoneNumber;
    ArrayList<ArrayList> contactList;

    UserClass(){
        contactList = new ArrayList<>() ;
        ArrayList<String> contacts = new ArrayList<>();
        contactList.add(contacts);
        contacts.add("test");
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String mail){

        email = mail;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String name){
        username = name;
    }
    public String getName(){
        return name;
    }
    public void setName(String na){
        name = na;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(String phone){
        phoneNumber = phone;
    }



}
