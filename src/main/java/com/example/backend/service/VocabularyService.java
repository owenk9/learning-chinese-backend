package com.example.backend.service;

import com.example.backend.dto.response.VocabularyResponse;

import java.util.List;

public interface VocabularyService {
    List<VocabularyResponse> getAllVocabulary();
    List<VocabularyResponse> getVocabularyById(Long id);
}
