package com.example.dvmanager_1.Model;

import com.google.firebase.firestore.PropertyName;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Id_Model implements Serializable {

    @SerializedName("Id")
    private Long id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Age")
    private Long age;
    @SerializedName("Gender")
    private String gender;
    @SerializedName("Email")
    private String email;
    @SerializedName("Phone No")
    private Long Phone_No;
    @SerializedName("Admission No")
    private Long Admission_No;
    @SerializedName("Address")
    private String address;
    @SerializedName("Profile Picture")
    private String Profile_Picture;
    @SerializedName("Blocked")
    private Boolean blocked;

    public Id_Model() {
    }

    public Id_Model(Long id, String name, Long age, String gender, String email, Long phone_No, Long admission_No, String address, String profile_Picture, Boolean blocked) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        Phone_No = phone_No;
        Admission_No = admission_No;
        this.address = address;
        Profile_Picture = profile_Picture;
        this.blocked = blocked;
    }
    @PropertyName("Id")
    public Long getId() {
        return id;
    }
    @PropertyName("Id")
    public void setId(Long id) {
        this.id = id;
    }

    @PropertyName("Name")
    public String getName() {
        return name;
    }
    @PropertyName("Name")
    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("Age")
    public Long getAge() {
        return age;
    }
    @PropertyName("Age")
    public void setAge(Long age) {
        this.age = age;
    }

    @PropertyName("Gender")
    public String getGender() {
        return gender;
    }
    @PropertyName("Gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @PropertyName("Email")
    public String getEmail() {
        return email;
    }
    @PropertyName("Email")
    public void setEmail(String email) {
        this.email = email;
    }

    @PropertyName("Phone No")
    public Long getPhone_No() {
        return Phone_No;
    }
    @PropertyName("Phone No")
    public void setPhone_No(Long phone_No) {
        Phone_No = phone_No;
    }

    @PropertyName("Admission No")
    public Long getAdmission_No() {
        return Admission_No;
    }
    @PropertyName("Admission No")
    public void setAdmission_No(Long admission_No) {
        Admission_No = admission_No;
    }

    @PropertyName("Address")
    public String getAddress() {
        return address;
    }
    @PropertyName("Address")
    public void setAddress(String address) {
        this.address = address;
    }

    @PropertyName("Profile Picture")
    public String getProfile_Picture() {
        return Profile_Picture;
    }
    @PropertyName("Profile Picture")
    public void setProfile_Picture(String profile_Picture) {
        Profile_Picture = profile_Picture;
    }

    @PropertyName("Blocked")
    public Boolean getBlocked() {
        return blocked;
    }
    @PropertyName("Blocked")
    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public String toString() {
        String result = "name: " + getName() + "\n";
        result += "id: " + getId()  + "\n";
        result += "email: " + getEmail() + "\n";
        result += "age: " + getAge() + "\n";
        result += "gender: " + getGender() + "\n";
        result += "address: " + getAddress() + "\n";
        result += "Admission No: " + getAdmission_No() + "\n";
        result += "Phone No: " + getPhone_No() + "\n";
        result += "Profile Picture: " + getProfile_Picture() + "\n";
        return result;
    }
}
