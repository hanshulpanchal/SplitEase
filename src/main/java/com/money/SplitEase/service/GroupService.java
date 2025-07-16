package com.money.SplitEase.service;

import com.money.SplitEase.model.Group;
import com.money.SplitEase.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    public Group createGroup(Group group) {
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

    public List<Group> getGroupsByUserId(Long userId) {
        return groupRepository.findByMembers_Id(userId);
    }
}
