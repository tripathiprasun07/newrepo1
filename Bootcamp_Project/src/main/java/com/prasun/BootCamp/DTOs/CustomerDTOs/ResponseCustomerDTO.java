package com.prasun.BootCamp.DTOs.CustomerDTOs;

public class ResponseCustomerDTO {

    private Long id;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;

    public ResponseCustomerDTO(Long id, String email, String firstName, String middleName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public ResponseCustomerDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
