package com.money.SplitEase.service;

import com.money.SplitEase.model.Group;
import com.money.SplitEase.model.User;
import com.money.SplitEase.repository.GroupRepository;
import com.money.SplitEase.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GroupService groupService;

    private User sampleUser;
    private Group sampleGroup;

    @BeforeEach
    void setup() {
        sampleUser = User.builder()
                .id(1L)
                .username("john")
                .email("john@example.com")
                .password("secret")
                .build();

        sampleGroup = Group.builder()
                .id(1L)
                .name("Trip")
                .members(Set.of(sampleUser))
                .expenses(Collections.emptySet())
                .build();

        // Correct mock setup
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(groupRepository.findByMembersContaining(sampleUser)).thenReturn(List.of(sampleGroup));
    }

    @Test
    void sampleTest() {
        List<Group> groups = groupService.getGroupsForUser(1L);
        assertNotNull(groups);
        assertEquals(1, groups.size());
        assertEquals("Trip", groups.get(0).getName());
    }
}
