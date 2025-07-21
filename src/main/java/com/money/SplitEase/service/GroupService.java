package com.money.SplitEase.service;

import com.money.SplitEase.dto.GroupDTO;
import com.money.SplitEase.model.Group;
import com.money.SplitEase.model.User;
import com.money.SplitEase.repository.GroupRepository;
import com.money.SplitEase.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Transactional
    public Group createGroup(GroupDTO dto) {
        System.out.println("Creating group with name: " + dto.getName());
        System.out.println("Member usernames: " + dto.getMemberNames());

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new RuntimeException("Group name is required");
        }
        if (dto.getMemberNames() == null || dto.getMemberNames().isEmpty()) {
            throw new RuntimeException("At least one member is required");
        }

        Group group = new Group();
        group.setName(dto.getName());

        Set<User> members = dto.getMemberNames().stream()
                .map(username -> {
                    System.out.println("Looking for user: " + username);
                    return userRepository.findByUsernameIgnoreCase(username)
                            .orElseThrow(() -> new RuntimeException("User not found: " + username));
                })
                .collect(Collectors.toSet());

        group.setMembers(members);

        System.out.println("Saving group with members: " + members.size());
        return groupRepository.save(group);
    }



    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> updateGroup(Long id, Group updatedGroup) {
        return groupRepository.findById(id).map(existing -> {
            existing.setName(updatedGroup.getName());
            existing.setMembers(updatedGroup.getMembers());
            existing.setExpenses(updatedGroup.getExpenses());
            return groupRepository.save(existing);
        });
    }

    public boolean deleteGroup(Long id) {
        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Group> getGroupByName(String name) {
        return groupRepository.findByName(name);
    }

    public List<Group> getGroupsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return groupRepository.findByMembersContaining(user);
    }

    public List<Group> getGroupsByUserId(Long userId) {
        return groupRepository.findByMembers_Id(userId);
    }
}
