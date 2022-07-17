package com.aaronr92.accountservice.service;

import com.aaronr92.accountservice.entity.Event;
import com.aaronr92.accountservice.repository.EventRepository;
import com.aaronr92.accountservice.util.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AuditService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getSecurityEvents() {
        List<Event> eventList = (List<Event>) eventRepository.findAll();
        eventList.sort(Comparator.comparing(Event::getId));     //sort by id in ascending order
        return eventList;
    }

    public void logEvent(Action action,
                         @Nullable String subject,
                         String object,
                         String path) {
        Event event = new Event();
        event.setAction(action);
        event.setSubject(subject == null ? "Anonymous" : subject);
        event.setObject(object);
        event.setPath(path);
        eventRepository.save(event);
    }
}
