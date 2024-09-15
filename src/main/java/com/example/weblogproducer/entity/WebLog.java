package com.example.weblogproducer.entity;

import lombok.Data;

import java.util.Date;

@Data
public class WebLog {
    private String ipAddress;
    private String url;
    private Date timestamp;
    private String userId;
    private String sessionId;
}
