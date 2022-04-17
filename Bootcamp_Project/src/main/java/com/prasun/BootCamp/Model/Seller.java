package com.prasun.BootCamp.Model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Seller extends Auditable<String> {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true)
    @NotBlank(message = "Enter Your GST")
    private String gst;



    @NotBlank(message = "Enter Your Company Contact")
    private String companyContact;
    @NotBlank(message = "Enter Your Company Name")
    private String companyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }


    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}