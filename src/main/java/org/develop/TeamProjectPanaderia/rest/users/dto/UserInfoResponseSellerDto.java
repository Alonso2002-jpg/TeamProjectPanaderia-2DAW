package org.develop.TeamProjectPanaderia.rest.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.TeamProjectPanaderia.rest.users.model.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseSellerDto {
    private Long id;
    private String name;
    private String username;
    private String email;
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);
    @Builder.Default
    private Boolean isActive = false;
    @Builder.Default
    private List<String> pedidos = new ArrayList<>();
}
