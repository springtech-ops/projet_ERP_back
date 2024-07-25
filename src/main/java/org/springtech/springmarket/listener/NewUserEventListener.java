package org.springtech.springmarket.listener;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springtech.springmarket.event.NewUserEvent;
import org.springtech.springmarket.service.EventService;

import static org.springtech.springmarket.utils.RequestUtils.getIpAddress;
import static org.springtech.springmarket.utils.RequestUtils.getDevice;

@Component
@RequiredArgsConstructor
public class NewUserEventListener {
    private final EventService eventService;
    private final HttpServletRequest request;

    @EventListener
    public void onNewUserEvent(NewUserEvent event) {
        try {
            System.out.println("Received event with email: " + event.getEmail());
            System.out.println("Device: " + getDevice(request));
            System.out.println("IP Address: " + getIpAddress(request));
            eventService.addUserEvent(event.getEmail(), event.getType(), getDevice(request), getIpAddress(request));
        } catch (Exception e) {
            e.printStackTrace(); // Ou utilisez un logger pour enregistrer l'exception
        }
    }
}
