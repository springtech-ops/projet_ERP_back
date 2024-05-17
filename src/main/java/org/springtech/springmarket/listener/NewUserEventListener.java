package org.springtech.springmarket.listener;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
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
        eventService.addUserEvent(event.getEmail(), event.getType(), getDevice(request), getIpAddress(request));
    }
}
