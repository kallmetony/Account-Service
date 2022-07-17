package com.aaronr92.accountservice.repository;

import com.aaronr92.accountservice.entity.BreachedPassword;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreachedPasswordRepository extends CrudRepository<BreachedPassword, Long> {

    @Query
    Boolean existsBreachedPasswordsByPassword(String password);

    BreachedPassword findByPassword(String password);
}
