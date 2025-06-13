package com.openclassrooms.mddapi.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageResponseDto {
    private String message;
}
