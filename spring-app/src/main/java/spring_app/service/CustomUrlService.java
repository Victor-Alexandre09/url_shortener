package spring_app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring_app.exceptions.DuplicatedRegisterException;
import spring_app.model.CustomUrl;
import spring_app.respository.CustomUrlRepository;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class CustomUrlService {

    CustomUrlRepository repository;

    public CustomUrlService(CustomUrlRepository repository) {
        this.repository = repository;
    }

    public void save (String originalUrl, String shortenUrl) {
        CustomUrl url = new CustomUrl();
        url.setOriginalUrl(originalUrl);
        url.setShortUrl(shortenUrl);
        repository.save(url);
    }

    public String GenerateShortenUrl (String originalUrl) {
        if (repository.findByOriginalUrl(originalUrl).isPresent()) {
            throw new DuplicatedRegisterException("The URL " + originalUrl + " already has a shortened version");
        }
        return generateShortId();
    }

    private final String ALPHA_NUMERIC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int SHORT_ID_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();

    private String generateShortId() {
        StringBuilder shortId = new StringBuilder();

        do {
            shortId.setLength(0);
            for (int i = 0; i < SHORT_ID_LENGTH; i++) {
                int index = random.nextInt(ALPHA_NUMERIC.length());
                shortId.append(ALPHA_NUMERIC.charAt(index));
            }
        } while (repository.existsById(shortId.toString()));
        System.out.println(shortId);
        return shortId.toString();
    }

    @Transactional
    public Optional<CustomUrl> FindAndincrementClick(String id) {
            Optional<CustomUrl> urlOptional = repository.findById(id);
            urlOptional.ifPresent(
                    url -> {url.setClicks(url.getClicks() + 1);
                    repository.save(url);
            });
            return urlOptional;
    }

}
