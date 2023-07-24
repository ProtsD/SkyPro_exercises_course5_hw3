package ru.skypro.skypro_exercises_course5_hw3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.skypro_exercises_course5_hw3.service.ReportService;

@RestController
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping(value = "/employees/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void putReport(@RequestParam("file") MultipartFile file) {
        System.out.println("Размер файла: " + file.getSize());
        reportService.putReport(file);
    }

    @PostMapping("/report")
    public int putGeneralReport() {
        return reportService.putGeneralReport();
    }

    @GetMapping(value = "/report/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getJson(@PathVariable(name = "id") int id) {
        String fileName = "employee.json";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(reportService.getJson(id));
    }
}
