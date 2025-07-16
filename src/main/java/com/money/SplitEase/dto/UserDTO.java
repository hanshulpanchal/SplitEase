package com.money.SplitEase.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Set<Long> groupIds;
    private Set<String> groupNames;
}