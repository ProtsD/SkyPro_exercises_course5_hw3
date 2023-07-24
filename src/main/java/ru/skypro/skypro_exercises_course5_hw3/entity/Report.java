package ru.skypro.skypro_exercises_course5_hw3.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "report")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//    @Lob
//    @Column(name = "json_text", columnDefinition = "CLOB")
    @Column(name = "json_text")
    private String text;

    public Report(String text) {
        this.text = text;
    }
}
