package ru.skypro.skypro_exercises_course5_hw3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeFullInfo;
import ru.skypro.skypro_exercises_course5_hw3.dto.ReportDTO;
import ru.skypro.skypro_exercises_course5_hw3.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>, PagingAndSortingRepository<Employee, Integer> {
//public interface EmployeeRepository extends CrudRepository<Employee, Integer>, PagingAndSortingRepository<Employee, Integer> {

    @Query(value = "SELECT * FROM employee WHERE salary > :salary", nativeQuery = true)
    List<Employee> getSalaryHigherThan(@Param("salary") int input);

    @Query(value = "SELECT * FROM employee WHERE salary = (SELECT MAX(salary) FROM employee)", nativeQuery = true)
    List<Employee> getEmployeesWithHighestSalary();

    @Query("SELECT new ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeFullInfo(e.name, e.salary, p.name) " +
            "FROM Employee e JOIN FETCH Position p WHERE e.position = p and e.id = :id")
    EmployeeFullInfo getEmployeeFullInfo(@Param("id") Integer id);

    @Query("SELECT new ru.skypro.skypro_exercises_course5_hw3.dto.ReportDTO(" +
            "p.name, " +
            "COUNT(e.name), " +
            "MAX(e.salary), " +
            "MIN(e.salary), " +
            "AVG(e.salary)) " +
            "FROM Employee e " +
            "JOIN FETCH Position p " +
            "WHERE e.position = p " +
            "GROUP BY p.name")
    List<ReportDTO> putGeneralReport();

    @Query(value = "SELECT * FROM employee WHERE position_id = :positionId", nativeQuery = true)
    List<Employee> getEmployeesOnPosition(@Param("positionId") Integer positionId);
}
