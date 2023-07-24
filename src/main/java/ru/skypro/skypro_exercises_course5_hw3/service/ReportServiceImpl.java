package ru.skypro.skypro_exercises_course5_hw3.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeDTO;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeMapper;
import ru.skypro.skypro_exercises_course5_hw3.entity.Report;
import ru.skypro.skypro_exercises_course5_hw3.exception.ReportNotFoundException;
import ru.skypro.skypro_exercises_course5_hw3.repository.EmployeeRepository;
import ru.skypro.skypro_exercises_course5_hw3.repository.ReportRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
    private final EmployeeRepository employeeRepository;
    private final ReportRepository reportRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void putReport(MultipartFile file) {
        logger.info("Invoke putReport() with argument: file");
        try {
            List<EmployeeDTO> employeeDTO = objectMapper.readValue(file.getBytes(), new TypeReference<>(){});
            employeeDTO.stream().map(EmployeeMapper::toEmployee).forEach(employeeRepository::save);
            logger.debug("putReport() is processed ok");
        } catch (IOException e) {
            logger.error("Invoke putReport() with argument: file. There is an exception.");
            e.printStackTrace();
        }
    }

    @Override
    public int putGeneralReport() {
        logger.info("Invoke putGeneralReport() w/o arguments");
        String json = null;
        try {
            json = objectMapper.writeValueAsString(employeeRepository.putGeneralReport());
            logger.debug("putGeneralReport() is processed ok");
        } catch (JsonProcessingException ex) {
            logger.error("Invoke putGeneralReport() w/o arguments. There is not possible to process the json", ex);
            ex.printStackTrace();
        }
        Report report = new Report(json);
        reportRepository.save(report);
        return report.getId();
    }

    @Override
    public Resource getJson(int id) {
        logger.info("Invoke getJson() with argument: id={}", id);
        Optional<Report> file = reportRepository.findById(id);
        if (file.isPresent()) {
            String jsonText = reportRepository.findById(id).get().getText();
            logger.debug("getJson() is processed ok");
            return new ByteArrayResource(jsonText.getBytes());
        }
        throw new ReportNotFoundException();
    }
}
