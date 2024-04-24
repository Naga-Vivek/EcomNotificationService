package com.scaler.EcomNotificationService.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class SendEmailDto {
    private String from;
    private String to;
    private String subject;
    private String body;
}
