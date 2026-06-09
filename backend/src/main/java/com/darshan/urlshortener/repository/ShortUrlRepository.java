package com.darshan.urlshortener.repository;

import com.darshan.urlshortener.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ShortUrlRepository
        extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByShortCode(
            String shortCode);

    boolean existsByShortCode(
            String shortCode);

    @Query("""
       SELECT COALESCE(
              SUM(s.clickCount),0)
       FROM ShortUrl s
       """)
    Long getTotalClicks();

    Long countByExpiryDateBefore(
            LocalDateTime date);

    Optional<ShortUrl>
    findTopByOrderByClickCountDesc();

}