package com.money.SplitEase.dto.mapper;


import com.money.SplitEase.dto.UserDTO;
import com.money.SplitEase.model.Group;
import com.money.SplitEase.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setGroupIds(user.getGroups().stream().map(Group::getId).collect(Collectors.toSet()));
        dto.setGroupNames(user.getGroups().stream().map(Group::getName).collect(Collectors.toSet()));
        return dto;
    }

    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        // Note: You'll need to set the groups separately
        // as this requires fetching the actual Group entities from the database
        return user;
    }
}