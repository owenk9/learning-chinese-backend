package com.example.backend.repository;

import com.example.backend.entity.UserVocabulary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserVocabularyRepository extends JpaRepository<UserVocabulary, Long> {
    List<UserVocabulary> findByUserId(Long userId);
    Optional<UserVocabulary> findByUserIdAndVocabularyId(Long userId, Long vocabularyId);
    boolean existsByUserIdAndVocabularyId(Long userId, Long vocabularyId);

}
