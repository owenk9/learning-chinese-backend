package com.example.backend.service;

import com.example.backend.dto.response.UserVocabularyResponse;
import com.example.backend.dto.response.VocabularyResponse;

import java.util.List;

public interface UserVocabularyService {
    List<UserVocabularyResponse> findAllVocabularyByUserId(Long userId);
    UserVocabularyResponse addVocabularyToFlashCard(Long userId, Long vocabularyId);
}
