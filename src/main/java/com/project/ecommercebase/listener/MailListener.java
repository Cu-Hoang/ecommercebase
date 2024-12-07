package com.project.ecommercebase.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.project.ecommercebase.service.MailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class MailListener {
    MailService mailService;

    @KafkaListener(topics = "mail_topic", groupId = "mail_group")
    public void listenGroupFoo(String email) {
        mailService.sendSimpleMessage(email, "Test", "This is a test");
        System.out.println("Received Message in group mail_group: " + email);
    }
}
