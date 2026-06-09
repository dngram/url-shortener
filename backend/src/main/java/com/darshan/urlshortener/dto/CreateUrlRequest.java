package com.darshan.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateUrlRequest {

    @NotBlank
    private String originalUrl;

    private String customAlias;

    private LocalDateTime expiryDate;
}