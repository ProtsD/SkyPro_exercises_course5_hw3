package ru.skypro.skypro_exercises_course5_hw3.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeDTO;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeFullInfo;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeMapper;
import ru.skypro.skypro_exercises_course5_hw3.entity.Employee;
import ru.skypro.skypro_exercises_course5_hw3.entity.Position;
import ru.skypro.skypro_exercises_course5_hw3.repository.EmployeeRepository;
import ru.skypro.skypro_exercises_course5_hw3.repository.PositionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepositoryMock;
    @Mock
    private PositionRepository positionRepositoryMock;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @ParameterizedTest
    @MethodSource("addEmployees_ProvideParamsForTests")
    void addEmployees_ArraysOfEmployee_NoReturn(EmployeeDTO[] employeeDTO) {
        employeeService.addEmployees(employeeDTO);
        verify(employeeRepositoryMock, times(1)).saveAll(any());
    }

    public static Stream<Arguments> addEmployees_ProvideParamsForTests() {
        return Stream.of(

                Arguments.of(
                        (Object) new EmployeeDTO[]{
                                EmployeeMapper.fromEmployee(
                                        new Employee()
                                                .setId(123)
                                                .setName("Denis")
                                                .setSalary(400)
                                                .setPosition(new Position().setId(1).setName("Programmer1"))),
                                EmployeeMapper.fromEmployee(
                                        new Employee()
                                                .setId(123)
                                                .setName("Denis2")
                                                .setSalary(400)
                                                .setPosition(new Position().setId(2).setName("Programmer2")))
                        }),
                Arguments.of(
                        (Object) new EmployeeDTO[]{
                                EmployeeMapper.fromEmployee(
                                        new Employee()
                                                .setId(123)
                                                .setName("Denis")
                                                .setSalary(400)
                                                .setPosition(new Position().setId(1).setName("Programmer1")))
                        }),
                Arguments.of((Object) new EmployeeDTO[]{})
        );
    }

    @Test
    void addEmployee_EmployeeDTO_NoReturn() {
        EmployeeDTO employee = EmployeeMapper.fromEmployee(
                new Employee()
                        .setId(123)
                        .setName("Denis")
                        .setSalary(400)
                        .setPosition(new Position().setId(2).setName("Programmer1"))
        );
        employeeService.addEmployee(employee);
        verify(employeeRepositoryMock, times(1)).save(any());
    }

    @Test
    void putEmployee_IdAndEmployeeDTO_NoReturn() {

        when(employeeRepositoryMock.existsById(anyInt()))
                .thenReturn(true);
        when(positionRepositoryMock.getPositionByName(any(String.class)))
                .thenReturn(new Position().setId(2).setName("Programmer1"));

        Integer inputInteger = 1;
        EmployeeDTO inputEmployee = EmployeeMapper.fromEmployee(
                new Employee()
                        .setId(123)
                        .setName("Denis")
                        .setSalary(400)
                        .setPosition(new Position().setId(2).setName("Programmer1"))
        );
        employeeService.putEmployee(inputInteger, inputEmployee);
        verify(employeeRepositoryMock, times(1)).save(any());
    }

    @Test
    void getEmployee_EmployeeId_ShouldReturnEmployeeDTO() {
        Integer input = 2;
        EmployeeDTO expectedDTO =
                EmployeeMapper.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Denis1")
                        .setSalary(400)
                        .setPosition(new Position().setId(2).setName("Programmer1")));

        Optional<Employee> expectedEntity =
                Optional.ofNullable(new Employee()
                        .setId(123)
                        .setName("Denis1")
                        .setSalary(400)
                        .setPosition(new Position().setId(2).setName("Programmer1"))
                );
        when(employeeRepositoryMock.findById(input))
                .thenReturn(expectedEntity);


        EmployeeDTO actual = employeeService.getEmployee(input);
        assertEquals(expectedDTO, actual);
        verify(employeeRepositoryMock, times(2)).findById(any());
    }

    @Test
    void delEmployee_ById_NoReturn() {
        Integer input = 1;
        employeeService.delEmployee(input);
        verify(employeeRepositoryMock, times(1)).deleteById(any());
    }

    @Test
    void getEmployeesWithHighestSalary_ValidPosition_ShouldReturnEmployeeDTOList() {
        List<EmployeeDTO> expected = List.of(
                EmployeeMapper.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Denis1")
                        .setSalary(402)
                        .setPosition(new Position().setId(2).setName("Programmer1"))),
                EmployeeMapper.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Denis2")
                        .setSalary(402)
                        .setPosition(new Position().setId(2).setName("Programmer2")))
        );

        List<Employee> expectedRepositoryOut = List.of(
                new Employee()
                        .setId(123)
                        .setName("Denis1")
                        .setSalary(402)
                        .setPosition(new Position().setId(2).setName("Programmer1")),
                new Employee()
                        .setId(123)
                        .setName("Denis2")
                        .setSalary(402)
                        .setPosition(new Position().setId(2).setName("Programmer2"))
        );
        Integer input = 2;
        when(employeeRepositoryMock.getEmployeesWithHighestSalary())
                .thenReturn(expectedRepositoryOut);

        List<EmployeeDTO> actual = employeeService.getEmployeesWithHighestSalary();
        assertEquals(expected, actual);
        verify(employeeRepositoryMock, times(1)).getEmployeesWithHighestSalary();
    }

    @Test
    void getEmployeesOnPosition_ValidPosition_ShouldReturnEmployeeDTOList() {
        List<EmployeeDTO> expectedMethodOut = List.of(
                EmployeeMapper.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Denis1")
                        .setSalary(400)
                        .setPosition(new Position().setId(2).setName("Programmer2"))),
                EmployeeMapper.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Denis2")
                        .setSalary(400)
                        .setPosition(new Position().setId(2).setName("Programmer2")))
        );

        List<Employee> expectedRepositoryOut = List.of(
                new Employee()
                        .setId(123)
                        .setName("Denis1")
                        .setSalary(400)
                        .setPosition(new Position().setId(2).setName("Programmer2")),
                new Employee()
                        .setId(123)
                        .setName("Denis2")
                        .setSalary(400)
                        .setPosition(new Position().setId(2).setName("Programmer2")),
                new Employee()
                        .setId(1234)
                        .setName("Denis3")
                        .setSalary(401)
                        .setPosition(new Position().setId(3).setName("Programmer3"))
        );

        String input = "Programmer2";
        when(positionRepositoryMock.getPositionByName(input))
                .thenReturn(new Position().setId(2).setName("Programmer2"));
//        when(employeeRepositoryMock.findAll())
//                .thenReturn(expectedRepositoryOut);
        when(employeeRepositoryMock.getEmployeesOnPosition(anyInt()))
                .thenReturn(expectedMethodOut.stream().map(EmployeeMapper::toEmployee).toList());

        List<EmployeeDTO> actual = employeeService.getEmployeesOnPosition(input);
        assertEquals(expectedMethodOut, actual);
        verify(employeeRepositoryMock, times(1)).getEmployeesOnPosition(anyInt());
    }

    @Test
    void getEmployeeFullInfo_ValidPosition_ShouldReturnEmployeeFullInfo() {
        Integer input = 3;
        EmployeeFullInfo expected = new EmployeeFullInfo()
                .setName("Salary")
                .setSalary(410000)
                .setPositionName("Developer1");

        when(employeeRepositoryMock.getEmployeeFullInfo(input))
                .thenReturn(expected);

        EmployeeFullInfo actual = employeeService.getEmployeeFullInfo(input);
        assertEquals(expected, actual);
        verify(employeeRepositoryMock, times(1)).getEmployeeFullInfo(any());
    }

    @Test
    void getEmployeePage_ValidPosition_ShouldReturnEmployeeFullInfo() {
        int page = 0;
        int size = 2;
        PageRequest input = PageRequest.of(page, size);
        List<EmployeeDTO> expected = List.of(
                EmployeeMapper.fromEmployee(new Employee()
                        .setId(12)
                        .setName("Denis1")
                        .setSalary(400)
                        .setPosition(new Position().setId(1).setName("Programmer1"))),
                EmployeeMapper.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Denis2")
                        .setSalary(500)
                        .setPosition(new Position().setId(2).setName("Programmer2")))
        );
        List<Employee> expectedRepositoryOut = List.of(
                new Employee()
                        .setId(12)
                        .setName("Denis1")
                        .setSalary(400)
                        .setPosition(new Position().setId(1).setName("Programmer1")),
                new Employee()
                        .setId(123)
                        .setName("Denis2")
                        .setSalary(500)
                        .setPosition(new Position().setId(2).setName("Programmer2"))
        );
        Page<Employee> pageContent = new PageImpl<>(expectedRepositoryOut);
        when(employeeRepositoryMock.findAll(input))
                .thenReturn(pageContent);

        List<EmployeeDTO> actual = employeeService.getEmployeePage(input);
        assertEquals(expected, actual);
        verify(employeeRepositoryMock, times(1)).findAll(input);
    }
}