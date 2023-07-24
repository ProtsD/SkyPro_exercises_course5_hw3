package ru.skypro.skypro_exercises_course5_hw3.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skypro.skypro_exercises_course5_hw3.entity.Employee;
import ru.skypro.skypro_exercises_course5_hw3.entity.Position;
import ru.skypro.skypro_exercises_course5_hw3.repository.EmployeeRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class EmployeeMapper {
    @Autowired
    private final EmployeeRepository employeeRepository;

    public static EmployeeDTO fromEmployee(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSalary(employee.getSalary());

        if (Optional.ofNullable(employee.getPosition()).isPresent()) {
            employeeDTO.setPosition(employee.getPosition().getName());
        }

        return employeeDTO;
    }

    public static Employee toEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setName(employeeDTO.getName());
        employee.setSalary(employeeDTO.getSalary());
        employee.setPosition(new Position().setName(employeeDTO.getPosition()));
        return employee;
    }

    public static Employee toEmployee(EmployeeDTO employeeDTO, Position position) {
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setName(employeeDTO.getName());
        employee.setSalary(employeeDTO.getSalary());
        employee.setPosition(position != null ? position : new Position().setName(employeeDTO.getPosition()));
        return employee;
    }
}
