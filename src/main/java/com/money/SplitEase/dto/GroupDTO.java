package com.money.SplitEase.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor    //  Required for JSON deserialization
@AllArgsConstructor   //  Good for testing / manual construction
public class GroupDTO {
    private Long id;

    @NotBlank(message = "Group name is required")
    private String name;


         // optional for response
    private Set<String> memberNames;    // used for input from frontend
}
