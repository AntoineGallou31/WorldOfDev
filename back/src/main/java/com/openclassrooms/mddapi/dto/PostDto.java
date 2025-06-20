package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.dto.response.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
    private String userUsername;
    private String subjectName;
    private List<CommentResponseDto> comments;
}
