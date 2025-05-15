package com.example.test.bank.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class UserProfileResponseDto {
    private String username;
    private String email;
    private Set<String> roles;
    private boolean active;

}
