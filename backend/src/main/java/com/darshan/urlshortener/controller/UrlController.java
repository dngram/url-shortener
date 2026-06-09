package com.darshan.urlshortener.controller;

import com.darshan.urlshortener.dto.CreateUrlRequest;
import com.darshan.urlshortener.dto.CreateUrlResponse;
import com.darshan.urlshortener.dto.UrlResponse;
import com.darshan.urlshortener.entity.ShortUrl;
import com.darshan.urlshortener.service.ShortUrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(
        origins = "http://localhost:4200"
)
@RestController
public class UrlController {

    private final ShortUrlService service;

    public UrlController(
            ShortUrlService service) {

        this.service = service;
    }

    @PostMapping("/api/urls")
    public CreateUrlResponse createUrl(
            @Valid
            @RequestBody
            CreateUrlRequest request) {

        return service.createShortUrl(
                request);
    }

    @GetMapping("/api/urls")
    public List<UrlResponse> getAllUrls() {

        return service.getAllUrls();
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @PathVariable
            String shortCode) {

        ShortUrl shortUrl =
                service.getByShortCode(
                        shortCode);

        service.incrementClickCount(
                shortUrl);

        return ResponseEntity
                .status(302)
                .header(
                        HttpHeaders.LOCATION,
                        shortUrl.getOriginalUrl())
                .build();
    }
}