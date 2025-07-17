package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.openclassrooms.mddapi.entity.Post;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT DISTINCT p FROM Post p " +
               "LEFT JOIN FETCH p.comments c " +
               "LEFT JOIN FETCH p.user " +
               "LEFT JOIN FETCH c.user " +
               "WHERE p.subject IN :subjects")
    List<Post> findBySubjectInWithCommentsAndAuthors(@Param("subjects") Collection<Subject> subjects);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.id = :id")
    Optional<Post> findByIdWithComments(Long id);
}
