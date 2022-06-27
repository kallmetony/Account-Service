package com.aaronr92.accountservice.repositories;

import com.aaronr92.accountservice.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query
    Optional<User> findUserByEmailIgnoreCase(String email);
}
