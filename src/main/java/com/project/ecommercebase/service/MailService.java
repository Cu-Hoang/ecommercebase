package com.project.ecommercebase.service;

import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.project.ecommercebase.dto.request.EmailMessage;
import com.project.ecommercebase.enums.ErrorCode;
import com.project.ecommercebase.exception.AppException;
import com.project.ecommercebase.service.impl.RedisServiceImpl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class MailService {
    JavaMailSender emailSender;

    RedisServiceImpl redisService;

    KafkaTemplate<String, Object> kafkaTemplate;

    public void sendSimpleMessage(String to, String subject, String text, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("cungochoang2002@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text + "\n" + content);
            emailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public String createCode(String email, String type, int second, String subject, String content) {
        SecureRandom secureRandom = new SecureRandom();
        int code = 100000 + secureRandom.nextInt(900000);

        StringBuilder key = new StringBuilder();
        key.append(type);
        key.append(email);
        redisService.setKeyWithTTL(String.valueOf(key), String.valueOf(code), second);

        try {
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send("mail_topic", new EmailMessage(email, subject, code, content));
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Sent message=[" + email + "] with offset=["
                            + result.getRecordMetadata().offset() + "]");
                } else {
                    log.error("Unable to send message=[" + email + "] due to : " + ex.getMessage());
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.CANNOT_SENDING_CODE);
        }
        return "Sent code successfully";
    }
}
