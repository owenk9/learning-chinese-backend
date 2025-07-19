package com.example.backend.service.Impl;

import com.example.backend.dto.response.UserVocabularyResponse;
import com.example.backend.entity.User;
import com.example.backend.entity.UserVocabulary;
import com.example.backend.entity.Vocabulary;
import com.example.backend.exception.DuplicateResourceException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.UserVocabularyMapper;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.UserVocabularyRepository;
import com.example.backend.repository.VocabularyRepository;
import com.example.backend.service.UserVocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserVocabularyServiceImpl implements UserVocabularyService {
    @Autowired
    private UserVocabularyRepository userVocabularyRepository;
    @Autowired
    private UserVocabularyMapper userVocabularyMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VocabularyRepository vocabularyRepository;

    @Override
    public List<UserVocabularyResponse> findAllVocabularyByUserId(Long userId) {
        List<UserVocabulary> vocabularyList = userVocabularyRepository.findByUserId(userId);
        return vocabularyList.stream()
                .map(userVocabularyMapper::toUserVocabularyResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserVocabularyResponse addVocabularyToFlashCard(Long userId, Long vocabularyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new RuntimeException("Vocabulary not found"));

        boolean exists = userVocabularyRepository.existsByUserIdAndVocabularyId(userId, vocabularyId);
        if (exists) {
            Vocabulary vocab = vocabularyRepository.findById(vocabularyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Vocabulary not found with id: " + vocabularyId));

            throw new DuplicateResourceException("Vocabulary '" + vocab.getWord() + "' already exists in flashcard");
        }



        UserVocabulary userVocabulary = new UserVocabulary();
        userVocabulary.setUser(user);
        userVocabulary.setVocabulary(vocabulary);
        userVocabulary.setIsLearned(false);
        userVocabulary.setIsFavorite(false);
        userVocabulary.setLastTimeStudied(null);

        UserVocabulary saved = userVocabularyRepository.save(userVocabulary);
        return userVocabularyMapper.toUserVocabularyResponse(saved);
    }

}
