package org.develop.TeamProjectPanaderia.rest.auth.services.users;

import org.develop.TeamProjectPanaderia.rest.auth.repositories.AuthRepository;
import org.develop.TeamProjectPanaderia.rest.users.exceptions.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class AuthUserServiceImpl implements AuthUserService{
    private final AuthRepository authRepository;

    @Autowired
    public AuthUserServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) {
        return authRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFound("username " + username));
    }
}
