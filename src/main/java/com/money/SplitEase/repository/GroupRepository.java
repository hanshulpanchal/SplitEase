package com.money.SplitEase.repository;


import com.money.SplitEase.model.Group;
import com.money.SplitEase.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    /**
     * Find a group by its exact name.
     */
    Optional<Group> findByName(String name);

    /**
     * Find all groups where a user is a member.
     */
    List<Group> findByMembers_Id(Long userId);


    List<Group> findByMembersContaining(User user);




}


