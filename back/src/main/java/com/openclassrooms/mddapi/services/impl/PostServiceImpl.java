package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.dto.request.CommentRequestDto;
import com.openclassrooms.mddapi.dto.request.PostRequestDto;
import com.openclassrooms.mddapi.dto.response.MessageResponseDto;
import com.openclassrooms.mddapi.dto.response.PostListResponseDto;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.PostNotFoundException;
import com.openclassrooms.mddapi.exception.SubjectNotFoundException;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.services.PostService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final SubjectRepository subjectRepository;

    @Autowired
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    @Override
    public PostListResponseDto getSubscribedPosts(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        Set<Subject> subscriptions = user.getSubscribedSubjects();
        List<Post> posts = postRepository.findBySubjectInWithCommentsAndAuthors(subscriptions);
        List<PostDto> postDtos = posts.stream()
                .map(PostMapper::toDto)
                .toList();

        return new PostListResponseDto(postDtos);
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.findByIdWithComments(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with ID: " + id));
        return PostMapper.toDto(post);
    }

    @Override
    public MessageResponseDto createPost(PostRequestDto postRequestDto, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        Subject subject = subjectRepository.findById(postRequestDto.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException("Subject not found"));

        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setUser(user);
        post.setSubject(subject);

        postRepository.save(post);

        return new MessageResponseDto().setMessage("Post created successfully");
    }

    @Override
    public MessageResponseDto addComment(Long postId, CommentRequestDto commentDto, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(user);
        comment.setPost(post);

        commentRepository.save(comment);

        return new MessageResponseDto().setMessage("Comment added successfully");
    }
}
