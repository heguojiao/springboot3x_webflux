package com.example.webflux.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final AtomicInteger counter = new AtomicInteger(0);

    @GetMapping(value = "/time", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamTime() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> {
                    int count = counter.incrementAndGet();
                    return String.format("Event #%d: Current time is %s", count, LocalDateTime.now());
                });
    }

    @GetMapping(value = "/numbers", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> streamNumbers() {
        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));
    }
} 