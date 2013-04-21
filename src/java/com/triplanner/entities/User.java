/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.entities;

/**
 *
 * @author brook
 */
public class User {
    public int id;
    public String email;
    public String firstname;
    public String lastname; 
    public String password;
    public int authority;
    
    public User(int id, String email, String firstname, String lastname, String password, int authority) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.authority = authority;
    }

    public int getId(){
        return id;
    }
    
    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPassword() {
        return password;
    }

    public int getAuthority() {
        return authority;
    }
    
}
