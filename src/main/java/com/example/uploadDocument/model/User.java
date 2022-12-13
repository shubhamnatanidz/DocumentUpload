package com.example.uploadDocument.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private int id;
    private String userName;
    private String password;
    private String role;

}
