package com.example.myapplication;

import android.content.Context;

public class Post {
    String Name;
    String College;
    String EnrolmentNo;
    String State;
     String Branch;
    String Year;
    String District;
    String link;
    String tittle;
    String description;
    String ProfileImg;

    public int getNewMessageCount() {
        return newMessageCount;
    }

    public void setNewMessageCount(int newMessageCount) {
        this.newMessageCount = newMessageCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    int newMessageCount;


    @Override
    public String toString() {
        return "Post{" +
                "Name='" + Name + '\'' +
                ", College='" + College + '\'' +
                ", EnrolmentNo='" + EnrolmentNo + '\'' +
                ", State='" + State + '\'' +
                ", Branch='" + Branch + '\'' +
                ", Year='" + Year + '\'' +
                ", District='" + District + '\'' +
                ", link='" + link + '\'' +
                ", tittle='" + tittle + '\'' +
                ", description='" + description + '\'' +
                ", ProfileImg='" + ProfileImg + '\'' +
                ", Id='" + Id + '\'' +
                ", context=" + context +
                '}';
    }

    public String getProfileImg() {
        return ProfileImg;
    }

    public void setProfileImg(String profileImg) {
        ProfileImg = profileImg;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    String Id;

    public Post(Context context) {
        this.context = context;
    }

    private Context context;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDiscription() {
        return description;
    }

    public void setDiscription(String description) {
        this.description = description;
    }

    public Post() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCollege() {
        return College;
    }

    public void setCollege(String college) {
        College = college;
    }

    public String getEnrolmentNo() {
        return EnrolmentNo;
    }

    public void setEnrolmentNo(String enrolmentNo) {
        EnrolmentNo = enrolmentNo;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

}
