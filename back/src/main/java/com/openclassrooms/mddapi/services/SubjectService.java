package com.openclassrooms.mddapi.services;


import com.openclassrooms.mddapi.dto.SubjectDto;
import com.openclassrooms.mddapi.dto.request.SubscribeSubjectRequestDto;
import com.openclassrooms.mddapi.dto.response.MessageResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface SubjectService {
    List<SubjectDto> getAllSubjectsWithSubscriptionStatus(UserDetails userDetails);

    MessageResponseDto subscribeToSubject(Long subjectId, UserDetails userDetails);

    MessageResponseDto unsubscribeFromSubject(Long subjectId, UserDetails userDetails);
}
