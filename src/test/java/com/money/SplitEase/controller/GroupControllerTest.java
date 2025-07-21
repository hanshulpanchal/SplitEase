package com.money.SplitEase.controller;
import com.money.SplitEase.dto.GroupDTO;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.money.SplitEase.model.Group;
import com.money.SplitEase.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GroupController.class)
@Import({GroupControllerTest.MockConfig.class, GroupControllerTest.TestSecurityConfig.class})
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GroupService groupService;

    private Group sampleGroup;

    @BeforeEach
    void setUp() {
        sampleGroup = Group.builder()
                .id(1L)
                .name("Test Group")
                .build();
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public GroupService groupService() {
            return Mockito.mock(GroupService.class);
        }
    }

    @Test
    void testCreateGroup() throws Exception {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(1L);
        groupDTO.setName("Test Group");
        groupDTO.setMemberNames(Set.of("TestUser", "TestUser2"));

        Group savedGroup = new Group();
        savedGroup.setId(1L);
        savedGroup.setName("Test Group");
        savedGroup.setMembers(new HashSet<>()); // You can mock members too if needed

        // Mock the service to return the saved group when receiving GroupDTO
        Mockito.when(groupService.createGroup(any(GroupDTO.class))).thenReturn(savedGroup);

        mockMvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Group"));
    }


    @Test
    void testGetGroupByIdFound() throws Exception {
        Mockito.when(groupService.getGroupById(1L)).thenReturn(Optional.of(sampleGroup));

        mockMvc.perform(get("/api/groups/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Group"));
    }

    @Test
    void testGetGroupByIdNotFound() throws Exception {
        Mockito.when(groupService.getGroupById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/groups/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllGroups() throws Exception {
        Mockito.when(groupService.getAllGroups()).thenReturn(List.of(sampleGroup));

        mockMvc.perform(get("/api/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testUpdateGroup() throws Exception {
        Mockito.when(groupService.updateGroup(eq(1L), any(Group.class))).thenReturn(Optional.of(sampleGroup));

        mockMvc.perform(put("/api/groups/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleGroup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testDeleteGroup() throws Exception {
        mockMvc.perform(delete("/api/groups/1"))
                .andExpect(status().isNoContent());
    }

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .csrf(csrf -> csrf.disable())
                    .httpBasic(Customizer.withDefaults());
            return http.build();
        }
    }

}
