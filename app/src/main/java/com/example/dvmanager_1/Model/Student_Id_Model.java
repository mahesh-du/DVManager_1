package com.example.dvmanager_1.Model;

import com.google.firebase.firestore.PropertyName;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Student_Id_Model extends Id_Model implements Serializable {

    @SerializedName("Person to contact in case of emergency")
    private String Person_to_contact_in_case_of_emergency;
    @SerializedName("Emergency Contact Number")
    private Long Emergency_Contact_Number;
    @SerializedName("Degree Program")
    private String Degree_Program;
    @SerializedName("Courses Enrolled In")
    private String Courses_Enrolled_In;
    @SerializedName("Interests")
    private String Interests;
    @SerializedName("DOB")
    private String DOB;

    public Student_Id_Model() {
    }

    public Student_Id_Model(Long id, String name, Long age, String gender, String email, Long phone_No, Long admission_No, String address, String profile_Picture, Boolean blocked, String person_to_contact_in_case_of_emergency, Long emergency_Contact_Number, String degree_Program, String courses_Enrolled_In, String interests, String DOB) {
        super(id, name, age, gender, email, phone_No, admission_No, address, profile_Picture, blocked);
        Person_to_contact_in_case_of_emergency = person_to_contact_in_case_of_emergency;
        Emergency_Contact_Number = emergency_Contact_Number;
        Degree_Program = degree_Program;
        Courses_Enrolled_In = courses_Enrolled_In;
        Interests = interests;
        this.DOB = DOB;
    }

    @PropertyName("Person to contact in case of emergency")
    public String getPerson_to_contact_in_case_of_emergency() {
        return Person_to_contact_in_case_of_emergency;
    }

    @PropertyName("Person to contact in case of emergency")
    public void setPerson_to_contact_in_case_of_emergency(String person_to_contact_in_case_of_emergency) {
        Person_to_contact_in_case_of_emergency = person_to_contact_in_case_of_emergency;
    }

    @PropertyName("Emergency Contact Number")
    public Long getEmergency_Contact_Number() {
        return Emergency_Contact_Number;
    }
    @PropertyName("Emergency Contact Number")
    public void setEmergency_Contact_Number(Long emergency_Contact_Number) {
        Emergency_Contact_Number = emergency_Contact_Number;
    }

    @PropertyName("Degree Program")
    public String getDegree_Program() {
        return Degree_Program;
    }
    @PropertyName("Degree Program")
    public void setDegree_Program(String degree_Program) {
        Degree_Program = degree_Program;
    }

    @PropertyName("Courses Enrolled In")
    public String getCourses_Enrolled_In() {
        return Courses_Enrolled_In;
    }
    @PropertyName("Courses Enrolled In")
    public void setCourses_Enrolled_In(String courses_Enrolled_In) {
        Courses_Enrolled_In = courses_Enrolled_In;
    }

    @PropertyName("Interests")
    public String getInterests() {
        return Interests;
    }
    @PropertyName("Interests")
    public void setInterests(String interests) {
        Interests = interests;
    }

    @PropertyName("DOB")
    public String getDOB() {
        return DOB;
    }

    @PropertyName("DOB")
    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String toString() {
        String result = "Name: " + getName() + "\n";
        result += "Id: " + getId()  + "\n";
        result += "Email: " + getEmail() + "\n";
        result += "Age: " + getAge() + "\n";
        result += "Gender: " + getGender() + "\n";
        result += "Address: " + getAddress() + "\n";
        result += "Admission No: " + getAdmission_No() + "\n";
        result += "Phone No: " + getPhone_No() + "\n";
        result += "Profile Picture: " + getProfile_Picture() + "\n";

        result += "Person_to_contact_in_case_of_emergency: " + getPerson_to_contact_in_case_of_emergency()  + "\n";
        result += "Emergency_Contact_Number: " + getEmergency_Contact_Number() + "\n";
        result += "Degree_Program: " + getDegree_Program() + "\n";
        result += "Courses_Enrolled_In: " + getCourses_Enrolled_In() + "\n";
        result += "Interests: " + getInterests() + "\n";
        result += "DOB: " + getDOB() + "\n";
        return result;
    }

}
