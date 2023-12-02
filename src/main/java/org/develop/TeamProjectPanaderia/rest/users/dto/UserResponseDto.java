package org.develop.TeamProjectPanaderia.rest.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.TeamProjectPanaderia.rest.users.model.Role;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    Long id;
    String name;
    String username;
    String email;
    String password;
    @Builder.Default
    Set<Role> roles = Set.of(Role.USER);
    @Builder.Default
    Boolean isActive = false;
}
