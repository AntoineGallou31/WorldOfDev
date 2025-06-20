package com.openclassrooms.mddapi.dto.response;

import com.openclassrooms.mddapi.dto.PostDto;
import lombok.Data;

@Data
public class PostListResponseDto {

    private Iterable<PostDto> posts;

    public PostListResponseDto(Iterable<PostDto> posts){
        this.posts = posts;
    }

}
