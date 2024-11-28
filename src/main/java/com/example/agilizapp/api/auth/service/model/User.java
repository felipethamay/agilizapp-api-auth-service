package com.example.agilizapp.api.auth.service.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String status;
}