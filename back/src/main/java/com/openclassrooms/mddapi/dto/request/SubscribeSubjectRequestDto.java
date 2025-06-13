package com.openclassrooms.mddapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeSubjectRequestDto {
    private Long subjectId;
}
