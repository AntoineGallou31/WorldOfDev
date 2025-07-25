package com.openclassrooms.mddapi.dto.response;

import com.openclassrooms.mddapi.dto.SubjectDto;
import lombok.Data;

@Data
public class SubjectsListResponseDto {

    private Iterable<SubjectDto> subjects;

    public SubjectsListResponseDto(Iterable<SubjectDto> subjects){
        this.subjects = subjects;
    }

}
