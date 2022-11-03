package com.project.paymebuddy.backend.dtos;

import com.project.paymebuddy.backend.entities.AppRole;
import com.project.paymebuddy.backend.entities.PayMyBuddyUser;

import java.util.List;

public interface UserDTO {

    record LoginRequest(String username, String password){}

    record SignupRequest(String name, String username, String password, AppRole appRole){}

    record JwtResponse(String token, String type, PayMyBuddyUser user, List<String> roles){}

}
