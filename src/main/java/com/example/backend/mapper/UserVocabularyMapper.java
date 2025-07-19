package com.example.backend.mapper;

import com.example.backend.dto.response.UserVocabularyResponse;
import com.example.backend.entity.UserVocabulary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = VocabularyMapper.class)
public interface UserVocabularyMapper {

    @Mapping(source = "vocabulary", target = "vocabulary")
    UserVocabularyResponse toUserVocabularyResponse(UserVocabulary userVocabulary);
}
