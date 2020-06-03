package com.example.dvmanager_1.Model;

import com.example.dvmanager_1.Constants;
import com.google.firebase.firestore.PropertyName;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import static com.example.dvmanager_1.Constants.firestore_IDS_FIELD_Image;

public class Scan_Add_Details_Model implements Serializable {

    @SerializedName("Name")
    private String Name;
    @SerializedName("Email")
    private String Email;
    @SerializedName("Phone Number")
    private Long Phone_No;
    @SerializedName("Admission No")
    private Long Admission_No;
    @SerializedName("Address")
    private String Address;
    @SerializedName("Gender")
    private String Gender;
    @SerializedName("Age")
    private Long Age;
    @SerializedName(firestore_IDS_FIELD_Image)
    private String profile_picture;
    @SerializedName("ID Proof")
    private String other_Id_Proof;
    @SerializedName("Blocked")
    private Boolean Blocked;

    public Scan_Add_Details_Model() {
    }

    public Scan_Add_Details_Model(String name, String email, Long phone_No, Long admission_No, String address, String gender, Long age, String profile_picture, String other_Id_Proof) {
        Name = name;
        Email = email;
        Phone_No = phone_No;
        Admission_No = admission_No;
        Address = address;
        Gender = gender;
        Age = age;
        this.profile_picture = profile_picture;
        this.other_Id_Proof = other_Id_Proof;
    }
    @PropertyName("Name")
    public String getname() {
        return Name;
    }
    @PropertyName("Name")
    public void setname(String name) {
        Name = name;
    }
    @PropertyName("Email")
    public String getemail() {
        return Email;
    }
    @PropertyName("Email")
    public void setemail(String email) {
        Email = email;
    }
    @PropertyName("Phone Number")
    public Long getphone_No() {
        return Phone_No;
    }
    @PropertyName("Phone Number")
    public void setphone_No(Long phone_No) {
        Phone_No = phone_No;
    }
    @PropertyName("Admission No")
    public Long getadmission_No() {
        return Admission_No;
    }
    @PropertyName("Admission No")
    public void setadmission_No(Long admission_No) {
        Admission_No = admission_No;
    }
    @PropertyName("Address")
    public String getaddress() {
        return Address;
    }
    @PropertyName("Address")
    public void setaddress(String address) {
        Address = address;
    }
    @PropertyName("Gender")
    public String getgender() {
        return Gender;
    }
    @PropertyName("Gender")
    public void setgender(String gender) {
        Gender = gender;
    }
    @PropertyName("Age")
    public Long getage() {
        return Age;
    }
    @PropertyName("Age")
    public void setage(Long age) {
        Age = age;
    }
    @PropertyName(firestore_IDS_FIELD_Image)
    public String getprofile_Picture() {
        return profile_picture;
    }
    @PropertyName(firestore_IDS_FIELD_Image)
    public void setprofile_Picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
    @PropertyName("ID Proof")
    public String getother_Id_Proof() {
        return other_Id_Proof;
    }
    @PropertyName("ID Proof")
    public void setother_Id_Proof(String other_Id_Proof) {
        this.other_Id_Proof = other_Id_Proof;
    }
    @PropertyName("Blocked")
    public Boolean getBlocked() {
        return Blocked;
    }
    @PropertyName("Blocked")
    public void setBlocked(Boolean blocked) {
        Blocked = blocked;
    }
}
