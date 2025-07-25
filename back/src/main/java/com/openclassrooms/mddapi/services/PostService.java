package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.dto.request.CommentRequestDto;
import com.openclassrooms.mddapi.dto.request.PostRequestDto;
import com.openclassrooms.mddapi.dto.response.MessageResponseDto;
import com.openclassrooms.mddapi.dto.response.PostListResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface PostService {
    PostListResponseDto getSubscribedPosts(UserDetails userDetails);

    PostDto getPostById(Long id);

    MessageResponseDto createPost(PostRequestDto postRequestDto, UserDetails userDetails);

    MessageResponseDto addComment(Long postId, CommentRequestDto postRequestDto, UserDetails userDetails);
}
