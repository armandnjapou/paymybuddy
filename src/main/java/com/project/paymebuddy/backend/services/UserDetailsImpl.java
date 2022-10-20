package com.project.paymebuddy.backend.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.paymebuddy.backend.entities.PayMyBuddyUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record UserDetailsImpl(Long id, String name, @Override String getUsername, @JsonIgnore @Override String getPassword,
                              Collection<? extends GrantedAuthority> authorities) implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    public static UserDetailsImpl build(PayMyBuddyUser user) {
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .toList();

        return new UserDetailsImpl(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
