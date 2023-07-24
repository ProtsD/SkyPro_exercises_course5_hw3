package ru.skypro.skypro_exercises_course5_hw3.repository;

import org.springframework.beans.factory.annotation.Autowired;

class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

/*    void getAllEmployeeByIdTest() {
        Optional<Employee> employee = employeeRepository.findById(1);
        assertTrue(employee.isEmpty());
        List<Employee> employeeListForSave = getEmployeeListForSave(expectedCounnt, targetName);

        int expectedId = 1;
        employee = employeeRepository.findById(1);
        assertEquals(expectedId, employee.orElseThrow());
        getEmployeeWithSalaryHigherThan
    }*/
}