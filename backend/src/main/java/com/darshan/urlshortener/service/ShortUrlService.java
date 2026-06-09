package com.darshan.urlshortener.service;

import com.darshan.urlshortener.dto.AnalyticsResponse;
import com.darshan.urlshortener.dto.CreateUrlRequest;
import com.darshan.urlshortener.dto.CreateUrlResponse;
import com.darshan.urlshortener.dto.UrlResponse;
import com.darshan.urlshortener.entity.ShortUrl;
import com.darshan.urlshortener.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.URL;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShortUrlService {

    private final ShortUrlRepository repository;
    private final RedisTemplate<String, ShortUrl> redisTemplate;

    public ShortUrlService(
            ShortUrlRepository repository, RedisTemplate<String, ShortUrl> redisTemplate) {

        this.repository = repository;
        this.redisTemplate = redisTemplate;
    }

    public CreateUrlResponse createShortUrl(
            CreateUrlRequest request) {

        try {

            new URL(
                    request.getOriginalUrl())
                    .toURI();

        }
        catch (Exception e) {

            throw new RuntimeException(
                    "Invalid URL");
        }

        String shortCode;

        if (
                request.getCustomAlias()
                        != null
                        &&
                        !request
                                .getCustomAlias()
                                .isBlank()
        ) {

            if (
                    repository
                            .existsByShortCode(
                                    request
                                            .getCustomAlias()
                            )
            ) {

                throw new RuntimeException(
                        "Alias already exists"
                );
            }

            shortCode =
                    request
                            .getCustomAlias();
        }
        else {

            shortCode =
                    generateUniqueCode();
        }

        ShortUrl shortUrl =
                ShortUrl.builder()
                        .originalUrl(
                                request.getOriginalUrl())
                        .shortCode(
                                shortCode)
                        .clickCount(0)
                        .createdAt(
                                LocalDateTime.now())
                        .expiryDate(
                                request.getExpiryDate())
                        .build();

        repository.save(shortUrl);

        return CreateUrlResponse.builder()
                .shortCode(shortCode)
                .shortUrl(
                        "http://localhost:8080/"
                                + shortCode)
                .build();
    }

    private String generateUniqueCode() {

        String characters =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                        + "abcdefghijklmnopqrstuvwxyz"
                        + "0123456789";

        SecureRandom random =
                new SecureRandom();

        String code;

        do {

            StringBuilder builder =
                    new StringBuilder();

            for (int i = 0; i < 6; i++) {

                builder.append(
                        characters.charAt(
                                random.nextInt(
                                        characters.length())));
            }

            code = builder.toString();

        } while (
                repository.existsByShortCode(
                        code));

        return code;
    }

    public ShortUrl getByShortCode(
            String shortCode) {

        ShortUrl cachedUrl =
                redisTemplate
                        .opsForValue()
                        .get(shortCode);

        if (cachedUrl != null) {

            System.out.println(
                    "Redis Cache HIT");

            if (
                    cachedUrl.getExpiryDate() != null
                            &&
                            cachedUrl.getExpiryDate()
                                    .isBefore(
                                            LocalDateTime.now())
            ) {

                redisTemplate.delete(
                        shortCode);

                throw new RuntimeException(
                        "URL has expired");
            }

            return cachedUrl;
        }

        System.out.println(
                "Redis Cache MISS");

        ShortUrl url =
                repository.findByShortCode(
                                shortCode)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Short URL not found"));

        if (
                url.getExpiryDate() != null
                        &&
                        url.getExpiryDate()
                                .isBefore(
                                        LocalDateTime.now())
        ) {

            throw new RuntimeException(
                    "URL has expired");
        }

        redisTemplate
                .opsForValue()
                .set(
                        shortCode,
                        url);

        return url;
    }
    public void incrementClickCount(
            ShortUrl shortUrl) {

        shortUrl.setClickCount(
                shortUrl.getClickCount() + 1);

        repository.save(shortUrl);
        redisTemplate
                .opsForValue()
                .set(
                        shortUrl.getShortCode(),
                        shortUrl);
    }

    public List<UrlResponse> getAllUrls() {

        return repository.findAll()
                .stream()
                .map(url ->
                        UrlResponse.builder()
                                .id(url.getId())
                                .originalUrl(
                                        url.getOriginalUrl())
                                .shortCode(
                                        url.getShortCode())
                                .clickCount(
                                        url.getClickCount())
                                .createdAt(
                                        url.getCreatedAt())
                                .expiryDate(
                                        url.getExpiryDate())
                                .build())
                .toList();
    }

    public AnalyticsResponse
    getAnalytics() {

        Long totalUrls =
                repository.count();

        Long totalClicks =
                repository.getTotalClicks();

        Long expiredUrls =
                repository
                        .countByExpiryDateBefore(
                                LocalDateTime.now());

        ShortUrl mostClicked =
                repository
                        .findTopByOrderByClickCountDesc()
                        .orElse(null);

        return AnalyticsResponse
                .builder()
                .totalUrls(totalUrls)
                .totalClicks(totalClicks)
                .expiredUrls(expiredUrls)
                .mostClickedShortCode(
                        mostClicked != null
                                ? mostClicked.getShortCode()
                                : "N/A")
                .highestClickCount(
                        mostClicked != null
                                ? mostClicked.getClickCount()
                                : 0)
                .build();
    }
}