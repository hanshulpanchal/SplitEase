package com.money.SplitEase.dto.mapper;


import com.money.SplitEase.dto.GroupDTO;
import com.money.SplitEase.model.Group;
import com.money.SplitEase.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GroupMapper {

    public GroupDTO toDTO(Group group) {
        GroupDTO dto = new GroupDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());

        dto.setMemberNames(group.getMembers().stream().map(User::getUsername).collect(Collectors.toSet()));
        return dto;
    }

    public Group toEntity(GroupDTO dto) {
        Group group = new Group();
        group.setId(dto.getId());
        group.setName(dto.getName());
        // Note: You'll need to set the members separately
        // as this requires fetching the actual User entities from the database
        return group;
    }
}