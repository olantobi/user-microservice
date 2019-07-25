package com.liferon.usermgt.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class UserDto {

    @NotEmpty(message = "Username is a required field")
    private String username;
    @NotEmpty(message = "Email is a required field")
    @Email
    private String email;        
    private boolean enabled = true;  		// DEFAULT
    @NotEmpty(message = "First name is a required field")
    private String firstName;
    @NotEmpty(message = "Last name is a required field")
    private String lastName;
    private String mobileNumber;
    @NotEmpty(message = "Password is a required field")
    private String password;
    /*@NotEmpty(message = "Confirm Password is a required field")
    private String confirmPassword;
*/
    private String tenantId;
    @NotEmpty(message = "Role is a required field")
    private String roleId;
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return this.enabled;
    }    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
/*
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
*/
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
/*
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
*/
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "UserDto{" + "username=" + username + ", email=" + email + ", enabled=" + enabled + ", firstName=" + firstName + ", lastName=" + lastName + ", mobileNumber=" + mobileNumber + ", password=" + password + ", tenantId=" + tenantId + ", roleId=" + roleId + '}';
    }

}
