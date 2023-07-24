package ru.skypro.skypro_exercises_course5_hw3.service;

import org.springframework.data.domain.PageRequest;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeDTO;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeFullInfo;

import java.util.List;

public interface EmployeeService {

    void addEmployee(EmployeeDTO employeeDTO);

    void addEmployees(EmployeeDTO[] employeeDTO);

    void putEmployee(Integer id, EmployeeDTO employeeDTO);

    EmployeeDTO getEmployee(Integer id);

    void delEmployee(Integer id);

    List<EmployeeDTO> getEmployeeWithSalaryHigherThan(int salary);

    List<EmployeeDTO> getEmployeesWithHighestSalary();

    List<EmployeeDTO> getEmployeesOnPosition(String position);

    EmployeeFullInfo getEmployeeFullInfo(Integer id);

    List<EmployeeDTO> getEmployeePage(PageRequest pageRequest);
}
