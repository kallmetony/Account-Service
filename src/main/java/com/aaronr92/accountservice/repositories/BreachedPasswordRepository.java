package com.aaronr92.accountservice.repositories;

import com.aaronr92.accountservice.entities.BreachedPassword;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreachedPasswordRepository extends CrudRepository<BreachedPassword, Long> {

    @Query
    Boolean existsBreachedPasswordsByPassword(String password);
}
