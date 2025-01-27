package com.project.ecommercebase.dto.request;

public record EmailMessage(String email, String subject, Integer code, String content) {}
