package com.aaronr92.accountservice.repositories;

import com.aaronr92.accountservice.entities.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
}
