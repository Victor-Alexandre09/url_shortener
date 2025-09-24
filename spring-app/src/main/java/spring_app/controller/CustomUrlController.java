package spring_app.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring_app.controller.dto.CustomUrlDto;
import spring_app.model.CustomUrl;
import spring_app.respository.CustomUrlRepository;
import spring_app.service.CustomUrlService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CustomUrlController {

    private final CustomUrlService customUrlService;
    private final CustomUrlRepository customUrlRepository;

    @Value("${app.domain}")
    private String domain;

    public CustomUrlController(CustomUrlService customUrlService, CustomUrlRepository customUrlRepository) {
        this.customUrlService = customUrlService;
        this.customUrlRepository = customUrlRepository;
    }

    @PostMapping("/shorten")
    public ResponseEntity<String> ShortenUrl (@RequestBody @Validated CustomUrlDto customUrlDto) {
        String originalUrl = customUrlDto.originalUrl();
        var shorternUrl = customUrlService.GenerateShortenUrl(originalUrl);
        customUrlService.save(originalUrl, shorternUrl);
        return ResponseEntity.ok(domain + "/" + shorternUrl);
    }

    @GetMapping("/{id}")
    public void redirect(@PathVariable String id, HttpServletResponse httpServletResponse) throws IOException {
        customUrlService.FindAndincrementClick(id).ifPresentOrElse(
                urlMapping -> {
                    try {
                        httpServletResponse.sendRedirect(urlMapping.getOriginalUrl());
                    } catch (IOException e) {
                        httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
                        e.getCause();
                    }
                },
                () -> {
                    httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
                }
        );
    }

    @GetMapping("/links")
    public ResponseEntity<List<CustomUrlDto>> list () {
        List<CustomUrl> urlsList = customUrlRepository.findAll();
        List<CustomUrlDto> dtos = urlsList.stream()
                .map(url -> new CustomUrlDto(url.getOriginalUrl(), url.getShortUrl(), url.getClicks()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}