package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "vocabulary")
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String word;
    private String pinyin;
    private String meaning;
    private String example;
    private String level;
    @OneToMany(mappedBy = "vocabulary")
    private List<UserVocabulary> vocabularyList;
}
