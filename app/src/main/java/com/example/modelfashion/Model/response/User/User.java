package com.example.modelfashion.Model.response.User;

public class User {
    private String id;
    private String userName;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private String sex;
    private String birthdate;
    private String address;
    private String totalSpend;
    private String avatar;
    private String activeStatus;
    private String accountType;

    public User(String id, String userName, String password, String email, String fullName, String phone, String sex, String birthdate, String address, String totalSpend, String avatar, String activeStatus, String accountType) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.sex = sex;
        this.birthdate = birthdate;
        this.address = address;
        this.totalSpend = totalSpend;
        this.avatar = avatar;
        this.activeStatus = activeStatus;
        this.accountType = accountType;
    }

    public User(String s, String s1, String s2, String s3, String s4, String s5, String s6) {

    }
    @Override
    public String toString() {
        return "{"+
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", sex='" + sex + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", address='" + address + '\'' +
                ", totalSpend='" + totalSpend + '\'' +
                ", avatar='" + avatar + '\'' +
                ", activeStatus='" + activeStatus + '\'' +
                ", accountType='"+accountType+'\''+
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(String totalSpend) {
        this.totalSpend = totalSpend;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
