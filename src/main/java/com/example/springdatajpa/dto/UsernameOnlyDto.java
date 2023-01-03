package com.example.springdatajpa.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsernameOnlyDto {
    private final String username;

    public String getUsername() {
        return username;
    }
}
