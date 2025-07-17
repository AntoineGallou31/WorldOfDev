package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.dto.request.CommentRequestDto;
import com.openclassrooms.mddapi.dto.request.PostRequestDto;
import com.openclassrooms.mddapi.dto.response.CommentResponseDto;
import com.openclassrooms.mddapi.dto.response.MessageResponseDto;
import com.openclassrooms.mddapi.dto.response.PostListResponseDto;
import com.openclassrooms.mddapi.dto.response.SubjectsListResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.services.PostService;

@Data
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private final PostService postService;

    @GetMapping("/list")
    public ResponseEntity<PostListResponseDto> getAllPosts(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        PostListResponseDto response = postService.getSubscribedPosts(userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(
            @Parameter(description = "User ID", required = true) @PathVariable Long id)
     {
        PostDto response = postService.getPostById(id);
         return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public MessageResponseDto createPost(
            @Valid @RequestBody PostRequestDto postRequestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        return postService.createPost(postRequestDto, userDetails);

    }

    @PostMapping("/{postId}/comment")
    public MessageResponseDto addComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequestDto commentDto,
            @AuthenticationPrincipal UserDetails userDetails
    )
    {
        return postService.addComment(postId, commentDto, userDetails);
    }
}
