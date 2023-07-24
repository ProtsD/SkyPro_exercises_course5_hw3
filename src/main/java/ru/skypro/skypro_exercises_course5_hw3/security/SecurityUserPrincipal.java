package ru.skypro.skypro_exercises_course5_hw3.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.skypro.skypro_exercises_course5_hw3.dto.UserDto;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Component
public class SecurityUserPrincipal implements UserDetails {
    private UserDto userDto;


    public SecurityUserPrincipal(UserDto userDto) {
        this.userDto = userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(userDto)
                .map(UserDto::getRole)
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .map(Collections::singleton)
                .orElseGet(Collections::emptySet);
    }

    @Override
    public String getPassword() {
        return Optional.ofNullable(userDto)
                .map(UserDto::getPassword)
                .orElse(null);
    }

    @Override
    public String getUsername() {
        return Optional.ofNullable(userDto)
                .map(UserDto::getLogin)
                .orElse(null);
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