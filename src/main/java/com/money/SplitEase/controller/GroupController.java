package com.money.SplitEase.controller;

import com.money.SplitEase.dto.GroupDTO;
import com.money.SplitEase.dto.mapper.GroupMapper;
import com.money.SplitEase.model.Group;
import com.money.SplitEase.model.User;
import com.money.SplitEase.repository.UserRepository;
import com.money.SplitEase.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Slf4j
public class GroupController {

    private final GroupService groupService;
    private final UserRepository userRepository;   // ✅ Injected properly
    private final GroupMapper groupMapper;         // ✅ Injected properly


    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody GroupDTO groupDTO) {
        try {
            Group savedGroup = groupService.createGroup(groupDTO);
            return ResponseEntity.ok(savedGroup);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong: " + e.getMessage()));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        Optional<Group> groupOpt = groupService.getGroupById(id);
        return groupOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<Group>> updateGroup(@PathVariable Long id, @Valid @RequestBody Group group) {
        log.info("Updating group ID {} with new name: {}", id, group.getName());
        Optional<Group> updatedGroup = groupService.updateGroup(id, group);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        log.warn("Deleting group with ID: {}", id);
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}
