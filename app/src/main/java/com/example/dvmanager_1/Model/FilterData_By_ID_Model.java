package com.example.dvmanager_1.Model;

import com.google.gson.annotations.SerializedName;

public class FilterData_By_ID_Model{

    @SerializedName("Id")
    private Long id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Profile Picture")
    private String Profile_Picture;
    private Boolean isChecked;

    public FilterData_By_ID_Model() {
        isChecked = false;
    }

    public FilterData_By_ID_Model(Long id, String name, String profile_Picture, Boolean isChecked) {
        this.id = id;
        this.name = name;
        Profile_Picture = profile_Picture;
        this.isChecked = isChecked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_Picture() {
        return Profile_Picture;
    }

    public void setProfile_Picture(String profile_Picture) {
        Profile_Picture = profile_Picture;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
