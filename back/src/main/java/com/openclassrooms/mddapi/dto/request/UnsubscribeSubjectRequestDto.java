package com.openclassrooms.mddapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnsubscribeSubjectRequestDto {
    private Long subjectId;
}
