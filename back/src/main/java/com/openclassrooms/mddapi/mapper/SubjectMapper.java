package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.SubjectDto;
import com.openclassrooms.mddapi.entity.Subject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SubjectMapper {
    public static SubjectDto toDto(Subject subject, Set<Long> userSubscribedSubjectIds) {
        boolean isSubscribed = userSubscribedSubjectIds.contains(subject.getId());

        SubjectDto dto = new SubjectDto();
        dto.setId(subject.getId());
        dto.setName(subject.getName());
        dto.setDescription(subject.getDescription());
        dto.setSubscribed(isSubscribed);
        return dto;
    }

    public static List<SubjectDto> toDtoList(List<Subject> subjects, Set<Subject> userSubscriptions) {
        Set<Long> userSubscribedSubjectIds = userSubscriptions.stream()
                .map(Subject::getId)
                .collect(Collectors.toSet());

        return subjects.stream()
                .map(subject -> toDto(subject, userSubscribedSubjectIds))
                .collect(Collectors.toList());
    }
}