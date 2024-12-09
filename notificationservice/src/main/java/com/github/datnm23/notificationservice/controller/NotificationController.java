package com.github.datnm23.notificationservice.controller;

import com.github.datnm23.notificationservice.model.MessageDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
    @PostMapping("/send-notification")
    public void sendNotification(@RequestBody MessageDTO messageDTO) {
    }
}
