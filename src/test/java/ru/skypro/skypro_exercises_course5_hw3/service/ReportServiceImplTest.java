package ru.skypro.skypro_exercises_course5_hw3.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeDTO;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeMapper;
import ru.skypro.skypro_exercises_course5_hw3.entity.Employee;
import ru.skypro.skypro_exercises_course5_hw3.entity.Position;
import ru.skypro.skypro_exercises_course5_hw3.entity.Report;
import ru.skypro.skypro_exercises_course5_hw3.exception.ReportNotFoundException;
import ru.skypro.skypro_exercises_course5_hw3.repository.EmployeeRepository;
import ru.skypro.skypro_exercises_course5_hw3.repository.ReportRepository;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepositoryMock;
    @Mock
    private ReportRepository reportRepositoryMock;
    @Mock
    private ObjectMapper objectMapperMock;
    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    void putReport_FileWithEmployeeDTOs_NoReturn() throws IOException {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[10]);

        List<EmployeeDTO> expected = List.of(
                EmployeeMapper.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Denis1")
                        .setSalary(400)
                        .setPosition(new Position().setId(2).setName("Programmer1"))),
                EmployeeMapper.fromEmployee(new Employee()
                        .setId(124)
                        .setName("Denis2")
                        .setSalary(400)
                        .setPosition(new Position().setId(2).setName("Programmer2")))
        );
        when(objectMapperMock.readValue(any(byte[].class), any(TypeReference.class)))
                .thenReturn(expected);
        reportService.putReport(file);
        verify(employeeRepositoryMock, times(2)).save(any(Employee.class));
    }

    @Test
    void putGeneralReport_NoReturn() throws JsonProcessingException {
        when(objectMapperMock.writeValueAsString(employeeRepositoryMock.putGeneralReport()))
                .thenReturn("test");
        reportService.putGeneralReport();
        verify(reportRepositoryMock, times(1)).save(any(Report.class));
    }

    @Test
    void getJson_ThrowReportNotFoundException() {
        Integer input = 20;
        when(reportRepositoryMock.findById(any()))
                .thenThrow(ReportNotFoundException.class);
        assertThrows(ReportNotFoundException.class, () -> reportRepositoryMock.findById(input));
    }
}