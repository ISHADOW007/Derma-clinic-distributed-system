package userservice.com.user.service;


import userservice.com.user.dto.AuthResponse;
import userservice.com.user.dto.LoginRequest;
import userservice.com.user.dto.SignupRequest;

public interface AuthService {
    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}
