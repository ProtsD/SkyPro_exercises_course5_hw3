package ru.skypro.skypro_exercises_course5_hw3.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EmployeeDTO {
    private Integer id;
    private String name;
    private Integer salary;
    private String position;
}
