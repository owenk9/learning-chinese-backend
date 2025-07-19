package com.example.backend.service.Impl;

import com.example.backend.dto.response.VocabularyResponse;
import com.example.backend.entity.Vocabulary;
import com.example.backend.mapper.VocabularyMapper;
import com.example.backend.repository.VocabularyRepository;
import com.example.backend.service.VocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VocabularyServiceImpl implements VocabularyService {
    @Autowired
    private VocabularyRepository vocabularyRepository;
    @Autowired
    private VocabularyMapper vocabularyMapper;

    @Override
    public List<VocabularyResponse> getAllVocabulary() {
        List<Vocabulary> vocabularyList = vocabularyRepository.findAll();
        return vocabularyList.stream().map(vocabularyMapper::toVocabularyResponse).collect(Collectors.toList());
    }

    @Override
    public List<VocabularyResponse> getVocabularyById(Long id) {
        return List.of();
    }
}
