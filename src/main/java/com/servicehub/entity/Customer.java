package com.servicehub.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false )
    private String email;

    @Column(unique = true, nullable = false)
    private String mobileNumber;


    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

public Customer(){

}
    public Customer (
            String firstName,
         String lastName,
         String email,
         String mobileNumber,
         String password
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber =mobileNumber;
        this.password = password;
    }
    public Long getId(){

    return id;
    }
    public String getFirstName() {

    return firstName;
    }
    public String getLastName(){

    return lastName;
    }
    public String getEmail(){
    return email;
    }
    public String getMobileNumber(){
    return mobileNumber;
    }

    public String getPassword(){
    return password;
    }

    public LocalDateTime getCreatedAt(){
     return  createdAt;
    }

    public void setId(Long id) {
        this.id= id;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public void setLastName(String lastName){
      this.lastName = lastName;
    }

    public void setEmail(String email){
    this.email=email;
    }

    public void setMobileNumber(String mobileNumber){
    this.mobileNumber = mobileNumber;
    }

    public void setPassword(String password){
    this.password = password;
    }

    public void setCreatedAt(LocalDateTime createdAt){
    this.createdAt = createdAt;
    }
}
