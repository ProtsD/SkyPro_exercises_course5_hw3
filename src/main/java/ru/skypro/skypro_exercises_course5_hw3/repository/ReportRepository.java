package ru.skypro.skypro_exercises_course5_hw3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.skypro_exercises_course5_hw3.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {

}
