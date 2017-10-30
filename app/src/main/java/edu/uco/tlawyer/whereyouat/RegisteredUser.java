package edu.uco.tlawyer.whereyouat;

public class RegisteredUser {

    private String name;
    private String phoneNum;

    public RegisteredUser(String name, String number) {
        this.name = name;
        this.phoneNum = number;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

}
