package com.darshan.urlshortener.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UrlResponse {

    private Long id;

    private String originalUrl;

    private String shortCode;

    private Integer clickCount;

    private LocalDateTime createdAt;
}