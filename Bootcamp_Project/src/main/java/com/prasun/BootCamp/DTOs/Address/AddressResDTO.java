package com.prasun.BootCamp.DTOs.Address;


public class AddressResDTO {
    private long id;
    private String city;
    private String state;
    private String country;
    private String addressLine;
    private String zipCode;


    public AddressResDTO(long id, String city, String state, String country, String addressLine, String zipCode) {
        this.id = id;
        this.city = city;
        this.state = state;
        this.country = country;
        this.addressLine = addressLine;
        this.zipCode = zipCode;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


}
