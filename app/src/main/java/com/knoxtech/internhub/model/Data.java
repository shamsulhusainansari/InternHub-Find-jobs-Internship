package com.knoxtech.internhub.model;

public class Data {
    private String applyUrl;
    private String bannerUrl;
    private String companyName;
    private String date;
    private String description;
    private String designation;
    private String location;
    private String logo;
    private String salary;
    private String documents;
    private String type;
    //private int priority;

    public Data() {
    }

    public Data(String applyUrl, String bannerUrl, String companyName, String date, String description, String designation, String location, String logo, String salary, String documents, String type) {
        this.applyUrl = applyUrl;
        this.bannerUrl = bannerUrl;
        this.companyName = companyName;
        this.date = date;
        this.description = description;
        this.designation = designation;
        this.location = location;
        this.logo = logo;
        this.salary = salary;
        this.documents = documents;
        this.type = type;
    }

    public String getApplyUrl() {
        return applyUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getDesignation() {
        return designation;
    }

    public String getLocation() {
        return location;
    }

    public String getLogo() {
        return logo;
    }

    public String getSalary() {
        return salary;
    }

    public String getDocuments() {
        return documents;
    }

    public String getType() {
        return type;
    }
}
