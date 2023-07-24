package ru.skypro.skypro_exercises_course5_hw3.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.skypro.skypro_exercises_course5_hw3.entity.Role;

@Component
@Getter
@Setter
public class UserDto {
    private int id;
    private String login;
    private String password;
    private Role role;
}
