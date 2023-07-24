package ru.skypro.skypro_exercises_course5_hw3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.skypro_exercises_course5_hw3.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByLogin(String login);
}
