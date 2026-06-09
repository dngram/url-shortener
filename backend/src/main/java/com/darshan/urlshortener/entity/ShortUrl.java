package com.darshan.urlshortener.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
@Table(
        name = "short_urls",
        indexes = {
                @Index(
                        name = "idx_short_code",
                        columnList = "shortCode"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortUrl implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String originalUrl;

    @Column(nullable = false, unique = true, length = 20)
    private String shortCode;

    @Column(nullable = false)
    private Integer clickCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime expiryDate;
}