package com.servicehub.dto;
import jakarta.validation.constraints.*;

public class ServiceProviderDTO {
    @NotBlank(message = "First Name is Required")
    private String firstName;

    @NotBlank(message = "Last Name is Required")
    private String lastName;

    @NotBlank(message = "Email is Required")
    @Email(message = "Enter Valid Email")
    private String email;

    @NotBlank(message = "Mobile Number is Required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile Number must be 10 digits"
    )
    private String mobileNumber;

    @NotBlank(message = "Password is Required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotNull(message = "Experience is Required")
    private Integer experience;

    @NotBlank(message = "City is Required")
    private String city;

    @NotBlank(message = "Address is Required")
    private String address;

    @NotNull(message = "Category Id is Required")
    private Long categoryId;


    public ServiceProviderDTO() {

    }

    public ServiceProviderDTO(
            String firstName,
            String lastName,
            String email,
            String mobileNumber,
            String password,
            Integer experience,
            String city,
            String address,
            Long categoryId) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.experience = experience;
        this.city = city;
        this.address = address;
        this.categoryId = categoryId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public  String getLastName(){
        return lastName;

    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }


    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }


}