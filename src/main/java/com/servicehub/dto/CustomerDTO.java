package com.servicehub.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerDTO {
    @NotBlank(message = "First Name Is Required")
    private String firstName;

    @NotBlank(message = "Last Name Is Required")
    private String lastName;

    @NotBlank(message = "Email Is Required")
    @Email(message = "Enter valid email")
    private String email;

    @NotBlank(message = "Mobile Number Is Required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile Number Must be 10 Digits"
    )
    private String mobileNumber;

    @NotBlank(message = "Password Is Required")
    @Size(min = 6,message = "Password Must Be At Least 6 Characters")
    private String password;

   public CustomerDTO(){

    }
  public CustomerDTO(
    String firstName,
    String lastName,
    String email,
    String mobileNumber,
    String password){

       this.firstName = firstName;
       this.lastName = lastName;
       this.email = email;
       this.mobileNumber = mobileNumber;
       this.password = password;

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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
       this.lastName = lastName;
    }
    public void setEmail(String email){
       this.email = email;
    }
    public void setMobileNumber(String mobileNumber){
       this.mobileNumber = mobileNumber;
    }
    public void setPassword(String password){
       this.password = password;
    }
}
