package com.money.SplitEase.service;

import com.money.SplitEase.model.Group;
import com.money.SplitEase.model.User;
import com.money.SplitEase.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupService groupService;

    private Group sampleGroup;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleUser = User.builder()
                .id(1L)
                .username("john")
                .email("john@example.com")
                .password("secret")
                .build();

        sampleGroup = Group.builder()
                .id(1L)
                .name("Trip")
                .members(Set.of((org.apache.catalina.User) sampleUser))
                .expenses(Collections.emptySet())
                .build();
    }

    @Test
    void testCreateGroup() {
        when(groupRepository.save(any(Group.class))).thenReturn(sampleGroup);

        Group result = groupService.createGroup(sampleGroup);
        assertNotNull(result);
        assertEquals("Trip", result.getName());
    }

    @Test
    void testGetGroupByIdFound() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(sampleGroup));

        Optional<Group> found = groupService.getGroupById(1L);
        assertTrue(found.isPresent());
        assertEquals("Trip", found.get().getName());
    }

    @Test
    void testGetGroupByIdNotFound() {
        when(groupRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Group> found = groupService.getGroupById(2L);
        assertTrue(found.isEmpty());
    }

    @Test
    void testGetAllGroups() {
        when(groupRepository.findAll()).thenReturn(List.of(sampleGroup));

        List<Group> groups = groupService.getAllGroups();
        assertEquals(1, groups.size());
    }

    @Test
    void testUpdateGroupWhenExists() {
        Group updatedGroup = Group.builder()
                .id(1L)
                .name("Trip 2025")
                .members(Set.of((org.apache.catalina.User) sampleUser))
                .expenses(Collections.emptySet())
                .build();

        when(groupRepository.findById(1L)).thenReturn(Optional.of(sampleGroup));
        when(groupRepository.save(any(Group.class))).thenReturn(updatedGroup);

        Optional<Group> result = groupService.updateGroup(1L, updatedGroup);
        assertTrue(result.isPresent());
        assertEquals("Trip 2025", result.get().getName());
    }

    @Test
    void testUpdateGroupWhenNotExists() {
        when(groupRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Group> result = groupService.updateGroup(99L, sampleGroup);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteGroupWhenExists() {
        when(groupRepository.existsById(1L)).thenReturn(true);
        doNothing().when(groupRepository).deleteById(1L);

        boolean deleted = groupService.deleteGroup(1L);
        assertTrue(deleted);
        verify(groupRepository).deleteById(1L);
    }

    @Test
    void testDeleteGroupWhenNotExists() {
        when(groupRepository.existsById(2L)).thenReturn(false);

        boolean deleted = groupService.deleteGroup(2L);
        assertFalse(deleted);
        verify(groupRepository, never()).deleteById(2L);
    }

    @Test
    void testGetGroupByNameFound() {
        when(groupRepository.findByName("Trip")).thenReturn(Optional.of(sampleGroup));

        Optional<Group> result = groupService.getGroupByName("Trip");
        assertTrue(result.isPresent());
        assertEquals("Trip", result.get().getName());
    }

    @Test
    void testGetGroupsByUserId() {
        when(groupRepository.findByMembers_Id(1L)).thenReturn(List.of(sampleGroup));

        List<Group> result = groupService.getGroupsByUserId(1L);
        assertEquals(1, result.size());
        assertEquals("Trip", result.get(0).getName());
    }
}
