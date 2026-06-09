package com.darshan.urlshortener.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalyticsResponse {

    private Long totalUrls;

    private Long totalClicks;

    private Long expiredUrls;

    private String mostClickedShortCode;

    private Integer highestClickCount;
}