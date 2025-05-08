package com.example.MualaFuel_Backend.dto;

import com.example.MualaFuel_Backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String nickName;
    private String email;
    private Set<Role> roles;
}
