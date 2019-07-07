package com.nuadu.rest.repository;

import com.nuadu.rest.model.Photo;
import com.nuadu.rest.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Query("SELECT p FROM Photo p WHERE p.user = :user")
    Page<Photo> findPaginated(@Param("user") User user, Pageable pageable);

    Photo findFirstByNameAndUser(String name, User user);
}
