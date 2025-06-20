package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.dto.response.CommentResponseDto;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.Post;

import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {

    public static PostDto toDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(dto.getId());
        dto.setTitle(dto.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());

        dto.setSubjectName(post.getSubject().getName());

        List<CommentResponseDto> commentDtos = post.getComments().stream()
                .map(PostMapper::mapCommentToDto)
                .collect(Collectors.toList());
        dto.setComments(commentDtos);

        dto.setUserId(post.getUser() != null ? post.getUser().getId() : null);
        dto.setUserUsername(post.getUser().getUsername());

        return dto;
    }

    private static CommentResponseDto mapCommentToDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setAuthorUsername(comment.getUser().getUsername());
        return dto;
    }

    public static List<PostDto> toDtoList(List<Post> posts) {
        return posts.stream()
                .map(PostMapper::toDto)
                .collect(Collectors.toList());
    }
}
