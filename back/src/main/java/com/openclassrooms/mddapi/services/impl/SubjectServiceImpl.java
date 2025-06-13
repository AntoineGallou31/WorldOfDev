package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.dto.SubjectDto;
import com.openclassrooms.mddapi.dto.request.SubscribeSubjectRequestDto;
import com.openclassrooms.mddapi.dto.response.MessageResponseDto;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
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

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Transactional(readOnly = true) // Assure une session Hibernate active et optimise la lecture
    public List<SubjectDto> getAllSubjectsWithSubscriptionStatus(UserDetails userDetails) {
        // Get the id of the user from the UserDetails
        String email = userDetails.getUsername();
        User userFromEmail = userRepository.findByEmail(email);
        if (userFromEmail == null) {
            throw new UserNotFoundException("Utilisateur non trouvé");
        }
        Long userId = userFromEmail.getId();

        // 1. Récupérer TOUS les sujets de la base de données.
        // Cela va générer une requête SQL comme: SELECT * FROM SUBJECTS;
        List<Subject> allSubjects = subjectRepository.findAll();

        // 2. Récupérer l'utilisateur spécifié ET ses abonnements (sujets souscrits) en UNE SEULE requête.
        // C'est ici que le JOIN FETCH de UserRepository est utilisé.
        // Cela générera une requête SQL comme: SELECT u.*, s.* FROM USERS u JOIN user_subject us ON u.id = us.user_id JOIN SUBJECTS s ON us.subject_id = s.id WHERE u.id = ?;
        User user = userRepository.findByIdWithSubscriptions(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // 3. Extraire les IDs des sujets auxquels l'utilisateur est abonné dans un Set.
        // L'accès à user.getSubscriptions() ici est sûr car la collection a été FETCHée à l'étape 2.
        // L'utilisation d'un Set permet une vérification rapide (O(1)) de l'abonnement.
        Set<Long> subscribedSubjectIds = user.getSubscriptions().stream()
                .map(Subject::getId)
                .collect(Collectors.toSet());

        // 4. Parcourir tous les sujets (allSubjects) et construire les DTOs.
        // Pour chaque sujet, nous vérifions si son ID est présent dans 'subscribedSubjectIds'.
        return allSubjects.stream()
                .map(subject -> SubjectDto.builder()
                        .id(subject.getId())
                        .name(subject.getName())
                        .description(subject.getDescription())
                        .isSubscribed(subscribedSubjectIds.contains(subject.getId())) // Le résultat final désiré !
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MessageResponseDto subscribeToSubject(Long userId, SubscribeSubjectRequestDto subscribeSubjectRequestDto) {
        Long subjectId = subscribeSubjectRequestDto.getSubjectId();
        Optional<Subject> subject = subjectRepository.findById(subjectId);
        Optional<User> user = userRepository.findById(userId);

        User userEntity = user.orElseThrow(() -> new RuntimeException("User not found"));
        Subject subjectEntity = subject.orElseThrow(() -> new RuntimeException("Subject not found"));

        // Vérifie si l'utilisateur est déjà abonné
        if (userEntity.getSubscriptions().contains(subjectEntity)) {
            return new MessageResponseDto().setMessage("Utilisateur déjà abonné à ce sujet");
        }

        userEntity.getSubscriptions().add(subjectEntity);
        userRepository.save(userEntity);
        return new MessageResponseDto().setMessage("Subscription successful");
    }

    @Override
    @Transactional
    public MessageResponseDto unsubscribeFromSubject(Long userId, SubscribeSubjectRequestDto unsubscribeSubjectRequestDto) {
        Long subjectId = unsubscribeSubjectRequestDto.getSubjectId();
        Optional<Subject> subject = subjectRepository.findById(subjectId);
        Optional<User> user = userRepository.findById(userId);

        User userEntity = user.orElseThrow(() -> new RuntimeException("User not found"));
        Subject subjectEntity = subject.orElseThrow(() -> new RuntimeException("Subject not found"));

        // Vérifie si l'utilisateur est déjà abonné
        if (!userEntity.getSubscriptions().contains(subjectEntity)) {
            return new MessageResponseDto().setMessage("Utilisateur déjà desabonné à ce sujet");
        }

        userEntity.getSubscriptions().remove(subjectEntity);
        userRepository.save(userEntity);
        return new MessageResponseDto().setMessage("Unsubscription successful");
    }}
