package org.develop.TeamProjectPanaderia.rest.users.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * Clase que representa a un usuario en el sistema. Implementa la interfaz UserDetails
 * para la integración con Spring Security.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "USERS")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name cannot be empty")
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username cannot be empty")
    private String username;
    @NotBlank(message = "Email cannot be empty")
    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    @Column(nullable = false, unique = true)
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @Length(min = 8, message = "Password debe tener al menos 8 caracteres")
    @Column(nullable = false)
    private String password;

    @Builder.Default
    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Builder.Default
    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Builder.Default
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isActive = true;

    /**
     * Conjunto de roles asignados al usuario.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    /**
     * Devuelve una colección de roles con los que el usuario está asociado.
     *
     * @return Colección de roles del usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    /**
     * Obtiene el nombre de usuario del usuario.
     *
     * @return El nombre de usuario del usuario.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indica si la cuenta del usuario ha expirado.
     *
     * @return Si la cuenta del usuario no ha expirado (siempre devuelve true).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    /**
     * Indica si la cuenta del usuario está bloqueada.
     *
     * @return Si la cuenta del usuario no está bloqueada (siempre devuelve true).
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales del usuario han expirado.
     *
     * @return Si las credenciales del usuario no han expirado (siempre devuelve true).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si el usuario está habilitado.
     *
     * @return Si el usuario está habilitado (siempre devuelve true).
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
