package com.imag.privileges.repository;

import com.imag.privileges.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findByStatus(Boolean status);
    Optional<User> findByIdAndStatus(Long id, Boolean status);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}