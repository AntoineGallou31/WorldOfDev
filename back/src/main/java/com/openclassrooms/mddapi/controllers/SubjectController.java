package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.SubjectDto;
import com.openclassrooms.mddapi.dto.request.SubscribeSubjectRequestDto;
import com.openclassrooms.mddapi.dto.response.MessageResponseDto;
import com.openclassrooms.mddapi.dto.response.SubjectsListResponseDto;
import com.openclassrooms.mddapi.services.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    // Endpoint to retrieve all subjects
    @GetMapping
    @Operation(summary = "Get all subjects", responses = {
            @ApiResponse(responseCode = "200", description = "Subjects found", content = @Content(schema = @Schema(implementation = SubjectDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public SubjectsListResponseDto getAllSubjects(@AuthenticationPrincipal UserDetails userDetails) {
        List<SubjectDto> subjects = subjectService.getAllSubjectsWithSubscriptionStatus(userDetails);
        return new SubjectsListResponseDto(subjects);
    }

    @PostMapping("/{id}/subscribe")
    @Operation(summary = "Subscribe to a subject", responses = {
            @ApiResponse(responseCode = "200", description = "Subscription successful", content = @Content(schema = @Schema(implementation = SubjectDto.class))),
            @ApiResponse(responseCode = "404", description = "Subject not found")
    })
    public MessageResponseDto subscribeToSubject(
            @Parameter(description = "User ID", required = true) @PathVariable Long id,
            @RequestBody SubscribeSubjectRequestDto subscribeSubjectRequestDto) {
        return subjectService.subscribeToSubject(id, subscribeSubjectRequestDto);
    }

    @PostMapping("/{id}/unsubscribe")
    @Operation(summary = "Unsubscribe from a subject", responses = {
            @ApiResponse(responseCode = "200", description = "Unsubscription successful", content = @Content(schema = @Schema(implementation = SubjectDto.class))),
            @ApiResponse(responseCode = "404", description = "Subject not found")
    })
    public MessageResponseDto unsubscribeFromSubject(
            @Parameter(description = "User ID", required = true) @PathVariable Long id,
            @RequestBody SubscribeSubjectRequestDto unsubscribeSubjectRequestDto) {
        return subjectService.unsubscribeFromSubject(id, unsubscribeSubjectRequestDto);
    }

}
