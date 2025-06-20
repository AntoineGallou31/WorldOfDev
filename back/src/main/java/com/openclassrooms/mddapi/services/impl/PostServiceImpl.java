package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.dto.request.PostRequestDto;
import com.openclassrooms.mddapi.dto.response.MessageResponseDto;
import com.openclassrooms.mddapi.dto.response.PostListResponseDto;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.services.PostService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final SubjectRepository subjectRepository;

    @Override
    public PostListResponseDto getAllPosts() {
        List<Post> posts = postRepository.findAllWithCommentsAndAuthors();
        List<PostDto> postDtos = posts.stream()
                .map(PostMapper::toDto)
                .toList();
        return new PostListResponseDto(postDtos);
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.findByIdWithComments(id);
        return PostMapper.toDto(post);
    }

    @Override
    public MessageResponseDto createPost(PostRequestDto postRequestDto, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername());

        Subject subject = subjectRepository.findById(postRequestDto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setUser(user);
        post.setSubject(subject);

        postRepository.save(post);

        return new MessageResponseDto().setMessage("Post created successfully");
    }
}
