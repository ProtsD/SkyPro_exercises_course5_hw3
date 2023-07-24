package ru.skypro.skypro_exercises_course5_hw3.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeDTO;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeMapper;
import ru.skypro.skypro_exercises_course5_hw3.entity.Report;
import ru.skypro.skypro_exercises_course5_hw3.repository.EmployeeRepository;
import ru.skypro.skypro_exercises_course5_hw3.repository.ReportRepository;

import java.util.List;

import static AuxiliaryData.EmployeeAuxiliaryData.returnEmployeeListFromDB;
import static AuxiliaryData.EmployeeAuxiliaryData.returnJsonEmployeeDTOList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void putReport() throws Exception {
        int arrayElement1 = 1;
        int arrayElement2 = 2;
        String jsonString = returnJsonEmployeeDTOList(arrayElement1, arrayElement2);
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "Employee import file.json",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(objectMapper.readValue(jsonString, new TypeReference<List<EmployeeDTO>>() {
                })));


        mockMvc.perform(MockMvcRequestBuilders.multipart("/employees/upload")
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());


        List<EmployeeDTO> actual = returnEmployeeListFromDB(employeeRepository).stream().map(EmployeeMapper::fromEmployee).toList();
        assertThat(actual)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "position")
                .containsAnyElementsOf(objectMapper.readValue(jsonString, new TypeReference<List<EmployeeDTO>>() {
                }));
    }

    @Test
    void putGeneralReportAndGetJson() throws Exception {
        int arrayElement1 = 5;
        int arrayElement2 = 4;

        String jsonString = returnJsonEmployeeDTOList(arrayElement1, arrayElement2);
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "Employee import file.json",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(objectMapper.readValue(jsonString, new TypeReference<List<EmployeeDTO>>() {
                })));


        mockMvc.perform(post("/report"))
                .andExpect(status().isOk());
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).isNotEmpty();


        MvcResult result = mockMvc.perform(get("/report/" + reportList.get(reportList.size()-1).getId()))
                .andExpect(status().isOk())
                .andReturn();
        byte[] reportByteCode = result.getResponse().getContentAsByteArray();
        assertThat(reportByteCode).isNotEmpty();
    }
}