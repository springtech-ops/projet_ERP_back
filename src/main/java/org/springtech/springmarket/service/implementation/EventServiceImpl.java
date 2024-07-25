package org.springtech.springmarket.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springtech.springmarket.domain.UserEvent;
import org.springtech.springmarket.enumeration.EventType;
import org.springtech.springmarket.repository.EventRepository;
import org.springtech.springmarket.service.EventService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    @Override
    public Collection<UserEvent> getEventsByUserId(Long userId) {
        return eventRepository.getEventsByUserId(userId);
    }

    @Override
    public void addUserEvent(String email, EventType eventType, String device, String ipAddress) {
        try {
            eventRepository.addUserEvent(email, eventType, device, ipAddress);
        } catch (Exception e) {
            // Loggez l'erreur
            System.err.println("Error adding user event: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void addUserEvent(Long userId, EventType eventType, String device, String ipAddress) {

    }
}
