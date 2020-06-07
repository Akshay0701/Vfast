package com.example.vfast.Model;

public class ModelDeleBoy {
    String AddharNo,Email,Name,Phone,image,uid;

    public ModelDeleBoy() {
    }

    public ModelDeleBoy(String addharNo, String email, String name, String phone, String image, String uid) {
        AddharNo = addharNo;
        Email = email;
        Name = name;
        Phone = phone;
        this.image = image;
        this.uid = uid;
    }

    public String getAddharNo() {
        return AddharNo;
    }

    public void setAddharNo(String addharNo) {
        AddharNo = addharNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
