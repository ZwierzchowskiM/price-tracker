package com.mzwierzchowski.price_tracker.model.dtos;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String email;
    private String password;
}
