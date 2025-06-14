package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.dto.SubjectDto;
import com.openclassrooms.mddapi.dto.request.SubscribeSubjectRequestDto;
import com.openclassrooms.mddapi.dto.response.MessageResponseDto;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.SubjectNotFoundException;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.mapper.SubjectMapper;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.services.SubjectService;
import com.openclassrooms.mddapi.services.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Data
@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private final SubjectRepository subjectRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public List<SubjectDto> getAllSubjectsWithSubscriptionStatus(UserDetails userDetails) {
        Long userId = getUserIdFromUserDetails(userDetails);

        List<Subject> allSubjects = subjectRepository.findAll();

        User user = userRepository.findByIdWithSubscriptions(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        return SubjectMapper.toDtoList(allSubjects, user.getSubscriptions());
    }

    @Override
    @Transactional
    public MessageResponseDto subscribeToSubject(Long userId, SubscribeSubjectRequestDto subscribeSubjectRequestDto) {
        User user = getUserOrThrow(userId);
        Subject subject = getSubjectOrThrow(subscribeSubjectRequestDto.getSubjectId());

        if (user.getSubscriptions().contains(subject)) {
            return new MessageResponseDto().setMessage("User already subscribed to this subject");
        }

        user.getSubscriptions().add(subject);
        userRepository.save(user);
        return new MessageResponseDto().setMessage("Subscription successful");
    }

    @Override
    @Transactional
    public MessageResponseDto unsubscribeFromSubject(Long userId, SubscribeSubjectRequestDto unsubscribeSubjectRequestDto) {
        User user = getUserOrThrow(userId);
        Subject subject = getSubjectOrThrow(unsubscribeSubjectRequestDto.getSubjectId());

        if (!user.getSubscriptions().contains(subject)) {
            return new MessageResponseDto().setMessage("User already unsubscribed from this subject");
        }

        user.getSubscriptions().remove(subject);
        userRepository.save(user);
        return new MessageResponseDto().setMessage("Unsubscription successful");
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private Subject getSubjectOrThrow(Long subjectId) {
        return subjectRepository.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException("Subject not found"));
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        String email = userDetails.getUsername();
        User userFromEmail = userRepository.findByEmail(email);
        if (userFromEmail == null) {
            throw new UserNotFoundException("User email not found: " + email);
        }
        return userFromEmail.getId();
    }

}
