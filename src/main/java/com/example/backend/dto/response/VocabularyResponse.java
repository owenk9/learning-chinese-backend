package com.example.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VocabularyResponse {
    private Long id;
    private String word;
    private String pinyin;
    private String meaning;
    private String example;
    private String level;
}
