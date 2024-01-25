package com.imag.privileges.repository;

import com.imag.privileges.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group,Long> {
    List<Group> findByStatus(boolean status);

    Optional<Group> findByName(String name);

    Optional<Group> findByIdAndStatus(Long id, boolean b);

    boolean existsByName(String name);
}