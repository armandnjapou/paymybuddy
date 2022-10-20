package com.project.paymebuddy.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.paymebuddy.backend.entities.AppRole;

import java.util.List;

public interface UserDTO {

    record LoginRequest(String username, String password){}

    record SignupRequest(String name, String username, String password, AppRole appRole){}

    record JwtResponse(String token, String type, Long id, String username, String name, @JsonIgnore @Override String password, List<String> roles){}

    record MessageResponse(String message) {}

}
