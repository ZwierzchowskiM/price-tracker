package com.mzwierzchowski.price_tracker.model.dtos;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;

}
