package edu.wpi.request;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String email;
    private String password;
    private String firstname;
    private String lastname;
}