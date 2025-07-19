package com.example.backend.mapper;

import com.example.backend.dto.response.VocabularyResponse;
import com.example.backend.entity.Vocabulary;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VocabularyMapper {
//    Vocabulary toVocabulary(VocabularyResponse vocabularyResponse);
    VocabularyResponse toVocabularyResponse(Vocabulary vocabulary);
}
