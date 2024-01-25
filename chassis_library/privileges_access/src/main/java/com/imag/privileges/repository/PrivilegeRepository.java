package com.imag.privileges.repository;

import com.imag.privileges.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    List<Privilege> findByStatus(Boolean status);

    Optional<Privilege> findByName(String name);

    Optional<Privilege> findByIdAndStatus(Long id, boolean b);

    boolean existsByName(String name);

//        @Query(name = "select * from privileges where id in (select parent_id from privileges where status=:status)",nativeQuery = true)
//    @Query("SELECT p FROM Privilege p WHERE p.id IN (SELECT pg.parentId FROM Privilege pg)")
//    List<Privilege> getPrivilegesByParent();
}