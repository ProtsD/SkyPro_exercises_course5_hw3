package ru.skypro.skypro_exercises_course5_hw3.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeDTO;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeMapper;
import ru.skypro.skypro_exercises_course5_hw3.repository.EmployeeRepository;

import java.util.List;

import static AuxiliaryData.EmployeeAuxiliaryData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest(classes = SkyProExercisesCourse5Hw3Application.class)
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@WithMockUser
class EmployeeControllerTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanEmployeeTable() {
        employeeRepository.deleteAll();
    }

    private static final String EMPLOYEES_URL = "/employees";


    @Test
    void addEmployee_SingleRecord() throws Exception {
        int employeeId = 1;
        mockMvc.perform(post(EMPLOYEES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(returnJsonEmployeeDTO(employeeId).toString()))
                .andExpect(status().isOk());
        String expectedName = Integer.toString(employeeId);


        assertEquals(expectedName, returnEmployeeListFromDB(employeeRepository).stream().findFirst().orElseThrow().getName());
    }

    @Test
    void addEmployee_List() throws Exception {
        int arrayElement1 = 1;
        int arrayElement2 = 2;
        String jsonString = returnJsonEmployeeDTOList(arrayElement1, arrayElement2);
        mockMvc.perform(post(EMPLOYEES_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk());


        List<EmployeeDTO> actual = returnEmployeeListFromDB(employeeRepository).stream().map(EmployeeMapper::fromEmployee).toList();
        assertThat(actual)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactlyElementsOf(objectMapper.readValue(jsonString, new TypeReference<List<EmployeeDTO>>() {
                }));
    }

    @Test
    void putEmployee_SingleRecord() throws Exception {
        Integer employeeId = 1;
        String newEmployeeName = "3";
        mockMvc.perform(post(EMPLOYEES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(returnJsonEmployeeDTO(employeeId).toString()))
                .andExpect(status().isOk());


        employeeId = returnEmployeeListFromDB(employeeRepository).stream().findFirst().orElseThrow().getId();
        JSONObject jsonObject = returnJsonEmployeeDTO(employeeId);
        jsonObject.put("name", newEmployeeName);
        mockMvc.perform(put(EMPLOYEES_URL + "/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk());


        assertEquals(newEmployeeName, returnEmployeeListFromDB(employeeRepository).stream().findFirst().orElseThrow().getName());
    }

    @Test
    void getEmployee() throws Exception {
        int employeeId = 1;
        mockMvc.perform(post(EMPLOYEES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(returnJsonEmployeeDTO(employeeId).toString()))
                .andExpect(status().isOk());


        employeeId = returnEmployeeListFromDB(employeeRepository).stream().findFirst().orElseThrow().getId();
        mockMvc.perform(get(EMPLOYEES_URL + "/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employeeId));
    }

    @Test
    void delEmployee_ExistingRecord() throws Exception {
        int employeeId = 1;
        mockMvc.perform(post(EMPLOYEES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(returnJsonEmployeeDTO(employeeId).toString()))
                .andExpect(status().isOk());


        employeeId = returnEmployeeListFromDB(employeeRepository).stream().findFirst().orElseThrow().getId();
        mockMvc.perform(delete(EMPLOYEES_URL + "/{id}", employeeId))
                .andExpect(status().isOk());
    }

    @Test
    void delEmployee_Exception() throws Exception {
        int employeeId = 1;
        mockMvc.perform(delete(EMPLOYEES_URL + "/{id}", employeeId))
                .andExpect(status().isOk());
    }

    @Test
    void getEmployeeWithSalaryHigherThan_IfOneTwoOrNoneRecordsExist() throws Exception {
        int employeeId = 50;
        String jsonString = returnJsonEmployeeDTOList(employeeId, employeeId - 1);
        mockMvc.perform(post(EMPLOYEES_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk());


        int salary = employeeId - 1;
        mockMvc.perform(get(EMPLOYEES_URL + "/salaryHigherThan/{salary}", salary))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));


        salary = employeeId - 2;
        mockMvc.perform(get(EMPLOYEES_URL + "/salaryHigherThan/{salary}", salary))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));


        salary = employeeId + 1;
        mockMvc.perform(get(EMPLOYEES_URL + "/salaryHigherThan/{salary}", salary))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getEmployeesOnPosition_IfOneTwoOrNoneRecordsExist() throws Exception {
        int employeeId = 1;


        String jsonString = returnJsonEmployeeDTOList(employeeId, employeeId, employeeId + 1);
        mockMvc.perform(post(EMPLOYEES_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk());


        String position = Integer.toString(employeeId);
        mockMvc.perform(get(EMPLOYEES_URL + "?position={position}", position))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));


        position = Integer.toString(employeeId + 1);
        mockMvc.perform(get(EMPLOYEES_URL + "?position={position}", position))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getEmployeeFullInfo() throws Exception {
        int employeeId = 49;
        mockMvc.perform(post(EMPLOYEES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(returnJsonEmployeeDTO(employeeId).toString()))
                .andExpect(status().isOk());
        returnEmployeeListFromDB(employeeRepository).forEach(System.out::println);


        String name = returnEmployeeListFromDB(employeeRepository).stream().findFirst().orElseThrow().getName();
        employeeId = returnEmployeeListFromDB(employeeRepository).stream().findFirst().orElseThrow().getId();
        mockMvc.perform(get(EMPLOYEES_URL + "/{employeeId}/fullInfo", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    void getEmployeePage_3ElementsAreOnThePage0And2OnThePage1() throws Exception {
        Integer page0 = 0;
        Integer page1 = 1;
        Integer size = 3;
        String jsonString = returnJsonEmployeeDTOList(21, 22, 23, 24, 25);
        mockMvc.perform(post(EMPLOYEES_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk());


        System.out.println(jsonString);
        mockMvc.perform(get(EMPLOYEES_URL + "/page?page={page}&size={size}", page0, size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));


        System.out.println(jsonString);
        mockMvc.perform(get(EMPLOYEES_URL + "/page?page={page}&size={size}", page1, size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}