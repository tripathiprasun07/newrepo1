package com.prasun.BootCamp.Model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Customer extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "user_id")
    private ApplicationUser user;

    @NotBlank(message = "Enter Your Contact")
    String contact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public ApplicationUser getUser() {
        return user;
    }

}