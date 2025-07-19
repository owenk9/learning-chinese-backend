package com.example.backend.controller;

import com.example.backend.dto.response.UserVocabularyResponse;
import com.example.backend.service.UserVocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-vocabulary")
public class UserVocabularyController {
    @Autowired
    private UserVocabularyService userVocabularyService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserVocabularyResponse>> getLearnedVocabulary(@PathVariable Long userId) {
        List<UserVocabularyResponse> list = userVocabularyService.findAllVocabularyByUserId(userId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<UserVocabularyResponse> addVocabularyToFlashCard(@RequestParam Long userId, @RequestParam Long vocabularyId) {
        UserVocabularyResponse response = userVocabularyService.addVocabularyToFlashCard(userId, vocabularyId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
