package spring_app.controller.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record CustomUrlDto(
        @NotBlank(message = "URL não pode estar vazia")
        @URL
        String originalUrl,
        String shortUrl,
        int clicks
) {
}
