package com.openclassrooms.mddapi.dto.request;

import lombok.Data;

@Data
public class PostRequestDto {
    private String title;

    private String content;

    private Long subjectId;

}
