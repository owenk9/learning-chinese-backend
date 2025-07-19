package com.example.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserVocabularyResponse {
    private Long id;
    private VocabularyResponse vocabulary;
    private Boolean isLearned;
    private Boolean isFavourite;
    private LocalDateTime lastTimeStudied;
}
