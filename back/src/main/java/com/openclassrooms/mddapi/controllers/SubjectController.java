package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.SubjectDto;
import com.openclassrooms.mddapi.dto.response.MessageResponseDto;
import com.openclassrooms.mddapi.dto.response.SubjectsListResponseDto;
import com.openclassrooms.mddapi.services.SubjectService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private final SubjectService subjectService;

    // Endpoint to retrieve all subjects with subscription status for the authenticated user
    @GetMapping("/list")
    public SubjectsListResponseDto getAllSubjects(@AuthenticationPrincipal UserDetails userDetails) {
        List<SubjectDto> subjects = subjectService.getAllSubjectsWithSubscriptionStatus(userDetails);
        return new SubjectsListResponseDto(subjects);
    }

    // Endpoint to subscribe to a subject
    @PostMapping("/{id}/subscribe")
    public MessageResponseDto subscribeToSubject(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return subjectService.subscribeToSubject(id, userDetails);
    }

    // Endpoint to unsubscribe from a subject
    @PostMapping("/{id}/unsubscribe")
    public MessageResponseDto unsubscribeFromSubject(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return subjectService.unsubscribeFromSubject(id, userDetails);
    }
}
