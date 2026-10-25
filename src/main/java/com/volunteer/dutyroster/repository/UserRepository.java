package com.volunteer.dutyroster.repository;

import com.volunteer.dutyroster.model.User;
import com.volunteer.dutyroster.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByRole(UserRole role);

    List<User> findByActiveTrue();

    List<User> findByRoleAndActiveTrue(UserRole role);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByFullNameContainingIgnoreCase(String name);
}
