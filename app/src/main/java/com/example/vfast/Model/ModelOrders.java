package com.example.vfast.Model;

public class ModelOrders {
    String User_Name,PickUp_Address,EndUp_Address, PickUp_phone,EndAddPhone,ProductWeight, uid, User_Email, Product, ProductKey,Status,Allocated_DeleID,ProductPrice,DeleComment,PickUpDate,PickUpTime,PickUpComment,EndUpDate,EndUpTime,EndUpComment;


    public ModelOrders(String user_Name, String pickUp_Address, String endUp_Address, String pickUp_phone, String endAddPhone, String productWeight, String uid, String user_Email, String product, String productKey, String status, String allocated_DeleID, String productPrice, String deleComment, String pickUpDate, String pickUpTime, String pickUpComment, String endUpDate, String endUpTime, String endUpComment) {
        User_Name = user_Name;
        PickUp_Address = pickUp_Address;
        EndUp_Address = endUp_Address;
        PickUp_phone = pickUp_phone;
        EndAddPhone = endAddPhone;
        ProductWeight = productWeight;
        this.uid = uid;
        User_Email = user_Email;
        Product = product;
        ProductKey = productKey;
        Status = status;
        Allocated_DeleID = allocated_DeleID;
        ProductPrice = productPrice;
        DeleComment = deleComment;
        PickUpDate = pickUpDate;
        PickUpTime = pickUpTime;
        PickUpComment = pickUpComment;
        EndUpDate = endUpDate;
        EndUpTime = endUpTime;
        EndUpComment = endUpComment;
    }

    public String getPickUpDate() {
        return PickUpDate;
    }

    public void setPickUpDate(String pickUpDate) {
        PickUpDate = pickUpDate;
    }

    public String getPickUpTime() {
        return PickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        PickUpTime = pickUpTime;
    }

    public String getPickUpComment() {
        return PickUpComment;
    }

    public void setPickUpComment(String pickUpComment) {
        PickUpComment = pickUpComment;
    }

    public String getEndUpDate() {
        return EndUpDate;
    }

    public void setEndUpDate(String endUpDate) {
        EndUpDate = endUpDate;
    }

    public String getEndUpTime() {
        return EndUpTime;
    }

    public void setEndUpTime(String endUpTime) {
        EndUpTime = endUpTime;
    }

    public String getEndUpComment() {
        return EndUpComment;
    }

    public void setEndUpComment(String endUpComment) {
        EndUpComment = endUpComment;
    }

    public String getDeleComment() {
        return DeleComment;
    }

    public void setDeleComment(String deleComment) {
        DeleComment = deleComment;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public ModelOrders() {
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getAllocated_DeleID() {
        return Allocated_DeleID;
    }

    public void setAllocated_DeleID(String allocated_DeleID) {
        Allocated_DeleID = allocated_DeleID;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getPickUp_Address() {
        return PickUp_Address;
    }

    public void setPickUp_Address(String pickUp_Address) {
        PickUp_Address = pickUp_Address;
    }

    public String getEndUp_Address() {
        return EndUp_Address;
    }

    public void setEndUp_Address(String endUp_Address) {
        EndUp_Address = endUp_Address;
    }

    public String getPickUp_phone() {
        return PickUp_phone;
    }

    public void setPickUp_phone(String pickUp_phone) {
        PickUp_phone = pickUp_phone;
    }

    public String getEndAddPhone() {
        return EndAddPhone;
    }

    public void setEndAddPhone(String endAddPhone) {
        EndAddPhone = endAddPhone;
    }

    public String getProductWeight() {
        return ProductWeight;
    }

    public void setProductWeight(String productWeight) {
        ProductWeight = productWeight;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser_Email() {
        return User_Email;
    }

    public void setUser_Email(String user_Email) {
        User_Email = user_Email;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getProductKey() {
        return ProductKey;
    }

    public void setProductKey(String productKey) {
        ProductKey = productKey;
    }
}
