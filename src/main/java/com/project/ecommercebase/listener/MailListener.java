package com.project.ecommercebase.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.project.ecommercebase.dto.request.EmailVerificationRequest;
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
    public void listenGroupMail(EmailVerificationRequest emailVerify) {
        mailService.sendSimpleMessage(
                emailVerify.email(), "Email Verification", String.valueOf(emailVerify.verificationCode()));
    }
}
