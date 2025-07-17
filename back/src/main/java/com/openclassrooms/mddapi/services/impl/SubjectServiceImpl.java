package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.dto.SubjectDto;
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
        if (userDetails == null) {
            throw new IllegalArgumentException("Les informations d'utilisateur sont requises");
        }

        Long userId = getUserIdFromUserDetails(userDetails);

        List<Subject> allSubjects = subjectRepository.findAll();
        if (allSubjects.isEmpty()) {
            throw new SubjectNotFoundException("Aucun sujet trouvé");
        }

        User user = userRepository.findByIdWithSubscriptions(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));

        return SubjectMapper.toDtoList(allSubjects, user.getSubscriptions());
    }

    @Override
    @Transactional
    public MessageResponseDto subscribeToSubject(Long subjectId, UserDetails userDetails) {
        if (subjectId == null) {
            throw new IllegalArgumentException("L'ID du sujet est requis");
        }

        if (userDetails == null) {
            throw new IllegalArgumentException("Les informations d'utilisateur sont requises");
        }

        Long userId = getUserIdFromUserDetails(userDetails);
        Subject subject = getSubjectOrThrow(subjectId);
        User user = getUserOrThrow(userId);

        // Vérifier si l'utilisateur est déjà abonné
        if (user.getSubscriptions().contains(subject)) {
            throw new IllegalStateException("Vous êtes déjà abonné à ce sujet");
        }

        user.getSubscriptions().add(subject);
        userRepository.save(user);
        return new MessageResponseDto().setMessage("Abonnement réussi");
    }

    @Override
    @Transactional
    public MessageResponseDto unsubscribeFromSubject(Long subjectId, UserDetails userDetails) {
        if (subjectId == null) {
            throw new IllegalArgumentException("L'ID du sujet est requis");
        }

        if (userDetails == null) {
            throw new IllegalArgumentException("Les informations d'utilisateur sont requises");
        }

        Long userId = getUserIdFromUserDetails(userDetails);
        Subject subject = getSubjectOrThrow(subjectId);
        User user = getUserOrThrow(userId);

        // Vérifier si l'utilisateur est bien abonné
        if (!user.getSubscriptions().contains(subject)) {
            throw new IllegalStateException("Vous n'êtes pas abonné à ce sujet");
        }

        user.getSubscriptions().remove(subject);
        userRepository.save(user);
        return new MessageResponseDto().setMessage("Désabonnement réussi");
    }

    private User getUserOrThrow(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("L'ID utilisateur ne peut pas être null");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));
    }

    private Subject getSubjectOrThrow(Long subjectId) {
        if (subjectId == null) {
            throw new IllegalArgumentException("L'ID du sujet ne peut pas être null");
        }
        return subjectRepository.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException("Sujet non trouvé avec l'ID: " + subjectId));
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("Les informations d'utilisateur ne peuvent pas être null");
        }

        String email = userDetails.getUsername();
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("L'email de l'utilisateur est manquant");
        }

        User userFromEmail = userRepository.findByEmail(email);
        if (userFromEmail == null) {
            throw new UserNotFoundException("Utilisateur non trouvé avec l'email: " + email);
        }
        return userFromEmail.getId();
    }

}
