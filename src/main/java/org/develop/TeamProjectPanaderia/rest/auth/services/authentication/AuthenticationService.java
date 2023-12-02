package org.develop.TeamProjectPanaderia.rest.auth.services.authentication;

import org.develop.TeamProjectPanaderia.rest.auth.dto.JwtAuthResponseDto;
import org.develop.TeamProjectPanaderia.rest.auth.dto.UserSignInRequest;
import org.develop.TeamProjectPanaderia.rest.auth.dto.UserSignUpRequest;

public interface AuthenticationService {
    JwtAuthResponseDto signUp(UserSignUpRequest signUpRequestRequest);
    JwtAuthResponseDto signIn(UserSignInRequest signInRequest);
}
